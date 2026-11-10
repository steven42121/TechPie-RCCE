import 'package:flutter/material.dart';
import 'package:flutter_secure_storage_ohos/flutter_secure_storage_ohos.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:techpie/models/user_session.dart';
import 'package:techpie/pages/account_deletion_page.dart';
import 'package:techpie/services/auth_service.dart';
import 'package:techpie/services/debug_logger.dart';
import 'package:techpie/services/http_client.dart';
import 'package:techpie/services/storage_service.dart';
import 'package:techpie/services/uni_auth_service.dart';

class _ConfirmingAccount extends AuthService {
  _ConfirmingAccount(super.storage, super.http, super.uniAuth);

  int requests = 0;
  String? expectedUserId;

  @override
  Future<void> deleteAccount({required String expectedUserId}) async {
    requests++;
    this.expectedUserId = expectedUserId;
    await logout();
  }
}

class _DeletingUniAuth extends UniAuthService {
  int requests = 0;

  @override
  Future<void> deleteAccount(String token, String userId) async {
    requests++;
  }
}

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();
  late StorageService storage;
  late LoggingHttpClient http;
  late _ConfirmingAccount auth;

  setUp(() async {
    SharedPreferences.setMockInitialValues({'sync_enabled': true});
    FlutterSecureStorage.setMockInitialValues({});
    storage = StorageService(await SharedPreferences.getInstance());
    await storage.saveSession(UserSession(
      userId: 'geekpie/account',
      userName: 'Test user',
      schoolName: '上海科技大学',
      createdAt: DateTime.utc(2026),
    ));
    http = LoggingHttpClient(DebugLogger());
    auth = _ConfirmingAccount(storage, http, UniAuthService());
    await auth.loadSession();
  });

  tearDown(() {
    auth.dispose();
    http.close();
  });

  Future<void> pumpPage(WidgetTester tester) async {
    await tester.pumpWidget(MaterialApp(home: AccountDeletionPage(auth: auth)));
    await tester.pumpAndSettle();
    await tester.tap(find.widgetWithText(TextButton, '永久注销'));
    await tester.pumpAndSettle();
  }

  testWidgets('cancelling permanent deletion sends no request', (tester) async {
    await pumpPage(tester);
    expect(find.text('永久注销账号？'), findsOneWidget);
    await tester.tap(find.widgetWithText(FilledButton, '取消'));
    await tester.pumpAndSettle();
    expect(auth.requests, 0);
    expect(auth.isLoggedIn, isTrue);
  });

  testWidgets('confirming deletes the opened account exactly once', (tester) async {
    await pumpPage(tester);
    await tester.tap(find.descendant(
      of: find.byType(AlertDialog),
      matching: find.widgetWithText(TextButton, '永久注销'),
    ));
    await tester.pumpAndSettle();
    expect(auth.requests, 1);
    expect(auth.expectedUserId, 'geekpie/account');
    expect(auth.isLoggedIn, isFalse);
    expect(find.text('账号已注销'), findsOneWidget);
    expect(find.widgetWithText(TextButton, '永久注销'), findsNothing);
  });

  test('deletion refuses a changed identity before using a token', () async {
    final uniAuth = _DeletingUniAuth();
    final service = AuthService(storage, http, uniAuth);
    addTearDown(service.dispose);
    await service.loadSession();
    try {
      await service.deleteAccount(expectedUserId: 'geekpie/another-account');
      fail('A different account must not be deleted.');
    } on Exception {
      expect(uniAuth.requests, 0);
    }
    expect(service.isLoggedIn, isTrue);
  });
}