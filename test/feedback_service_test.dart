import 'dart:convert';
import 'dart:typed_data';

import 'package:flutter_secure_storage_ohos/flutter_secure_storage_ohos.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:http/http.dart' as http;
import 'package:http/testing.dart';
import 'package:package_info_plus/package_info_plus.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:techpie/models/user_session.dart';
import 'package:techpie/services/auth_service.dart';
import 'package:techpie/services/debug_logger.dart';
import 'package:techpie/services/feedback_service.dart';
import 'package:techpie/services/http_client.dart';
import 'package:techpie/services/storage_service.dart';
import 'package:techpie/services/uni_auth_service.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();
  late AuthService auth;
  late LoggingHttpClient loggingHttp;
  late List<http.BaseRequest> requests;
  late FeedbackService feedback;
  var uploadStatus = 200;
  var submitStatus = 200;

  setUp(() async {
    SharedPreferences.setMockInitialValues({});
    FlutterSecureStorage.setMockInitialValues({});
    final storage = StorageService(await SharedPreferences.getInstance());
    await storage.saveSession(
      UserSession(
        userId: 'geekpie/test-user',
        userName: 'Test user',
        schoolName: '上海科技大学',
        createdAt: DateTime.utc(2026),
        geekpieToken: 'test-token',
        geekpieExpiresAt: DateTime.now().add(const Duration(days: 1)).toIso8601String(),
      ),
    );
    PackageInfo.setMockInitialValues(
      appName: 'TechPie',
      packageName: 'club.geekpie.techpie',
      version: '26.2.110323',
      buildNumber: '26110323',
      buildSignature: '',
    );
    loggingHttp = LoggingHttpClient(DebugLogger());
    auth = AuthService(storage, loggingHttp, UniAuthService());
    await auth.loadSession();
    requests = [];
    uploadStatus = 200;
    submitStatus = 200;
    feedback = FeedbackService(
      auth,
      'https://feedback.example/api',
      client: MockClient((request) async {
        requests.add(request);
        if (request.url.path.endsWith('/images')) {
          return http.Response(
            jsonEncode({
              'success': uploadStatus == 200,
              'data': {'id': 'image-1'},
            }),
            uploadStatus,
          );
        }
        return http.Response(
          jsonEncode({
            'success': submitStatus == 200,
            if (submitStatus != 200) 'error': '登录已过期',
          }),
          submitStatus,
        );
      }),
    );
  });

  tearDown(() {
    auth.dispose();
    loggingHttp.close();
  });

  test('feedback keeps profile fields opt-in and reports the RCCE stamp', () async {
    await feedback.submit('  建议增加功能  ');
    final request = requests.single as http.Request;
    final body = jsonDecode(request.body) as Map<String, dynamic>;
    expect(request.headers['Authorization'], 'Bearer test-token');
    expect(body['message'], '建议增加功能');
    expect(body['includeUsername'], isFalse);
    expect(body['includeEmail'], isFalse);
    expect(body['version'], '26h2-110323');
    expect(body['images'], isEmpty);
  });

  test('a failed upload stays retryable and successful images are reused', () async {
    final image = FeedbackImage(Uint8List.fromList([1, 2, 3]));
    uploadStatus = 500;
    await expectLater(feedback.uploadImage(image), throwsA(isA<Exception>()));
    expect(image.failed, isTrue);
    expect(image.uploaded, isFalse);

    uploadStatus = 200;
    await feedback.uploadImage(image);
    expect(image.failed, isFalse);
    expect(image.uploaded, isTrue);
    await feedback.submit('带图反馈', images: [image]);
    expect(requests.where((request) => request.url.path.endsWith('/images')), hasLength(2));
    final body = jsonDecode((requests.last as http.Request).body) as Map<String, dynamic>;
    expect(body['images'], [
      {'id': 'image-1'},
    ]);
  });

  test('backend authentication errors remain actionable', () async {
    submitStatus = 401;
    await expectLater(
      feedback.submit('反馈内容'),
      throwsA(predicate((error) => error.toString().contains('登录已过期'))),
    );
  });
}
