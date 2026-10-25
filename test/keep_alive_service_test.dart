import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:techpie/services/keep_alive_service.dart';
import 'package:techpie/services/storage_service.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  const channel = MethodChannel('techpie/keep_alive');
  final calls = <MethodCall>[];

  late StorageService storage;
  late KeepAliveService keepAlive;

  setUp(() async {
    calls.clear();
    SharedPreferences.setMockInitialValues({});
    storage = StorageService(await SharedPreferences.getInstance());
    keepAlive = KeepAliveService(storage, channel: channel);
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (call) async {
      calls.add(call);
      return true;
    });
  });

  tearDown(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, null);
  });

  Object? arg(String key) =>
      (calls.single.arguments! as Map<Object?, Object?>)[key];

  test('the resident process is opt-in', () async {
    expect(keepAlive.enabled, isFalse);

    await keepAlive.setEnabled(true);

    expect(keepAlive.enabled, isTrue);
    expect(storage.keepAliveEnabled, isTrue);
    expect(calls, hasLength(1));
    expect(calls.single.method, 'setEnabled');
    expect(arg('value'), isTrue);
  });

  test('turning it off reaches the host too', () async {
    await keepAlive.setEnabled(true);
    calls.clear();

    await keepAlive.setEnabled(false);

    expect(keepAlive.enabled, isFalse);
    expect(arg('value'), isFalse);
  });

  test('boot re-applies whatever is stored', () async {
    await storage.setKeepAliveEnabled(true);

    await keepAlive.sync();

    expect(arg('value'), isTrue);
  });

  test('a host without the channel is not an error', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, null);

    await expectLater(keepAlive.setEnabled(true), completes);
  });
}
