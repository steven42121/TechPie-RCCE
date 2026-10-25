import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:techpie/services/background_refresher.dart';
import 'package:techpie/services/storage_service.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  const channel = MethodChannel('techpie/background');
  final calls = <String>[];

  late StorageService storage;

  setUp(() async {
    calls.clear();
    SharedPreferences.setMockInitialValues({});
    storage = StorageService(await SharedPreferences.getInstance());
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (call) async {
      calls.add(call.method);
      return true;
    });
  });

  tearDown(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, null);
  });

  test('the wake-up is scheduled while either reminder is on', () async {
    // Spending alerts are on by default; the island is opt-in.
    await syncBackgroundRefresh(storage);
    expect(calls, ['schedule']);

    await storage.setIslandEnabled(true);
    await syncBackgroundRefresh(storage);
    expect(calls, ['schedule', 'schedule']);
  });

  test('both reminders off drops the schedule', () async {
    await storage.setSpendingAlertsEnabled(false);
    await storage.setIslandEnabled(false);

    await syncBackgroundRefresh(storage);

    expect(calls, ['cancel']);
  });

  test('a host without the channel is not an error', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, null);

    await expectLater(syncBackgroundRefresh(storage), completes);
  });
}
