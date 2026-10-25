import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:techpie/features/campus_card/domain/models/bill_models.dart';
import 'package:techpie/features/campus_card/domain/money_fen.dart';
import 'package:techpie/services/spending_alert_service.dart';
import 'package:techpie/services/storage_service.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  const channel = MethodChannel('techpie/notification');
  final posted = <MethodCall>[];

  late StorageService storage;
  late SpendingAlertService alerts;

  TransactionRecord record(
    String id, {
    int fen = -1250,
    TransactionKind kind = TransactionKind.consumption,
    String merchant = '第一食堂',
    int? balanceFen,
  }) {
    return TransactionRecord(
      id: id,
      occurredAt: DateTime(2026, 10, 24, 12, 30),
      title: kind == TransactionKind.recharge ? '充值' : merchant,
      amount: MoneyFen(fen),
      kind: kind,
      merchantName: merchant,
      balance: balanceFen == null ? null : MoneyFen(balanceFen),
    );
  }

  Object? arg(int index, String key) =>
      (posted[index].arguments! as Map<Object?, Object?>)[key];

  String? bodyOf(int index) => arg(index, 'body') as String?;

  setUp(() async {
    debugDefaultTargetPlatformOverride = TargetPlatform.android;
    posted.clear();
    SharedPreferences.setMockInitialValues({});
    final prefs = await SharedPreferences.getInstance();
    storage = StorageService(prefs);
    alerts = SpendingAlertService(storage, channel: channel);
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (call) async {
      posted.add(call);
      return true;
    });
  });

  tearDown(() {
    debugDefaultTargetPlatformOverride = null;
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, null);
  });

  test('a new payment is announced with amount, place and balance', () async {
    await alerts.alertNewSpending([record('t1', balanceFen: 8800)]);

    expect(posted, hasLength(1));
    expect(posted.single.method, 'post');
    expect(arg(0, 'title'), '消费提醒');
    expect(bodyOf(0), '-12.50 · 第一食堂 · 余额 88.00');
    expect(arg(0, 'silent'), isFalse);
  });

  test('a positive amount is still shown as spending', () async {
    await alerts.alertNewSpending([record('t1', fen: 4500)]);

    expect(bodyOf(0), startsWith('-45.00'));
  });

  test('the same transaction is never announced twice', () async {
    await alerts.alertNewSpending([record('t1')]);
    expect(storage.alertedTransactionIds, contains('t1'));

    await alerts.alertNewSpending([record('t1')]);
    expect(posted, hasLength(1));
  });

  test('recharges and refunds are not spending', () async {
    await alerts.alertNewSpending([
      record('r1', fen: 10000, kind: TransactionKind.recharge),
      record('r2', fen: 500, kind: TransactionKind.refund),
    ]);

    expect(posted, isEmpty);
  });

  test('a burst posts three and names the rest', () async {
    await alerts.alertNewSpending([
      for (final id in ['a', 'b', 'c', 'd', 'e']) record(id, merchant: '档口$id'),
    ]);

    expect(posted, hasLength(SpendingAlertService.maxPerRefresh));
    expect(bodyOf(0), contains('档口a'));
    expect(bodyOf(2), contains('另有 2 笔'));
    // Only the first message of a burst makes a sound.
    expect(arg(0, 'silent'), isFalse);
    expect(arg(1, 'silent'), isTrue);
    // Folded ones count as announced, so they do not resurface as news.
    expect(storage.alertedTransactionIds, containsAll(<String>['d', 'e']));
  });

  test('turning the setting off stops the announcements', () async {
    await storage.setSpendingAlertsEnabled(false);

    await alerts.alertNewSpending([record('t1')]);

    expect(posted, isEmpty);
  });
}
