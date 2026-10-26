import 'dart:convert';

import 'package:flutter_test/flutter_test.dart';
import 'package:http/http.dart' as http;
import 'package:techpie/services/update_service.dart';
import 'package:techpie/utils/product_version.dart';

void main() {
  test('offers the published release when it is newer, without its checksums',
      () async {
    final requests = <http.BaseRequest>[];
    final service = UpdateService(
      client: _FakeClient((request) async {
        requests.add(request);
        return _jsonResponse([
          {
            'tag_name': 'v1.0.2+7',
            'name': 'v1.0.2',
            'body': '## 变更\n\n'
                '- 修好了某件事\n'
                '\n'
                '\n## SHA-256\n\n'
                '```text\n'
                'abc123  TechPie-1.0.2-android-arm64v8.apk\n'
                '```\n',
          },
        ]);
      }),
    );

    final release = await service.checkForUpdate('1.0.1-rc.2');

    expect(release, isNotNull);
    expect(release!.version, const ProductVersion(1, 0, 2, null, 7));
    expect(release.name, 'v1.0.2');
    // The checksum block the release pipeline writes into the same body is not
    // part of what a user is shown as the changelog.
    expect(release.notes, '## 变更\n\n- 修好了某件事');

    final request = requests.single;
    expect(
      request.url.toString(),
      'https://api.github.com/repos/steven42121/TechPie-RCCE/releases',
    );
    expect(request.headers['Accept'], contains('github+json'));
    expect(request.headers['User-Agent'], isNotEmpty);
  });

  test('says nothing is newer when the running build is the newest', () async {
    final service = UpdateService(
      client: _FakeClient(
        (_) async => _jsonResponse([
          {'tag_name': 'v1.0.1+5', 'name': 'v1.0.1', 'body': 'notes'},
        ]),
      ),
    );

    expect(await service.checkForUpdate('1.0.1'), isNull);
    expect(await service.checkForUpdate('1.0.2'), isNull);

    // But a candidate of the published version is not the newest thing: the
    // stable release it led to is newer, so it is offered.
    final fromCandidate =
        await service.checkForUpdate('1.0.1-rc.2');
    expect(fromCandidate!.version, const ProductVersion(1, 0, 1, null, 5));
  });

  test('reads a legacy tag shape and a candidate', () async {
    // The list view is what carries candidates at all — `releases/latest`
    // never returns them, and this fork's test builds are exactly those.
    final service = UpdateService(
      client: _FakeClient(
        (_) async => _jsonResponse([
          {
            'tag_name': 'android-v1.0.2-rc.3+9',
            'name': 'v1.0.2-rc.3',
            'body': 'n',
            'prerelease': true,
          },
        ]),
      ),
    );

    final release = await service.checkForUpdate('1.0.1');
    expect(release!.version, const ProductVersion(1, 0, 2, 3, 9));
  });

  test('offers a higher build of the same stable version', () async {
    // The bug this was written for: v1.0.1+13 landed, but a build on 1.0.1+12 saw
    // only "1.0.1" and was told it was up to date.
    final service = UpdateService(
      client: _FakeClient(
        (_) async => _jsonResponse([
          {'tag_name': 'v1.0.1+13', 'name': 'v1.0.1', 'body': 'n'},
        ]),
      ),
    );

    final release = await service.checkForUpdate('1.0.1+12');

    expect(release, isNotNull);
    expect(release!.version, const ProductVersion(1, 0, 1, null, 13));
    // The title says only v1.0.1, so it must be the tag ("v1.0.1+13") that names
    // the offer — otherwise "发现新版本 v1.0.1" reads as no change at all.
    expect(release.name, 'v1.0.1+13');

    // And a build that is already the newer one is told nothing.
    expect(
      await service.checkForUpdate('1.0.1+13'),
      isNull,
    );
  });

  test('refuses to prompt from a version it cannot read', () async {
    final service = UpdateService(
      client: _FakeClient(
        (_) async => _jsonResponse([
          {'tag_name': 'nightly', 'name': 'nightly'},
        ]),
      ),
    );

    expect(
      () => service.checkForUpdate('1.0.1'),
      throwsA(isA<UpdateCheckException>()),
    );
  });

  test('offers the highest version in the list, not merely the first entry',
      () async {
    // GitHub lists releases by publication date, not version order: an older
    // release can head the list, and stepping over it must not hide the newer
    // one behind it.
    final service = UpdateService(
      client: _FakeClient(
        (_) async => _jsonResponse([
          {'tag_name': 'v1.0.2+8', 'name': 'v1.0.2', 'body': 'older'},
          {
            'tag_name': 'v1.0.3-rc.1+9',
            'name': 'v1.0.3-rc.1',
            'body': 'newer',
            'prerelease': true,
          },
        ]),
      ),
    );

    final release =
        await service.checkForUpdate('1.0.2+8');

    expect(release!.version, const ProductVersion(1, 0, 3, 1, 9));
    expect(release.name, 'v1.0.3-rc.1');
    expect(release.notes, 'newer');
  });

  test('offers a stamped release over any semver build', () async {
    // The fork moved to date-stamped builds (26h2-102610 = 2026 H2, Oct 26,
    // 10:00); they began after 1.0.2, so they outrank every semver release.
    final service = UpdateService(
      client: _FakeClient(
        (_) async => _jsonResponse([
          {'tag_name': '26h2-102610', 'name': '26h2-102610', 'body': 'n'},
          {'tag_name': 'v1.0.2+14', 'name': 'v1.0.2', 'body': 'n'},
        ]),
      ),
    );

    final release = await service.checkForUpdate('1.0.2+13');
    expect(release, isNotNull);
    expect(release!.version, const BuildStamp(26, 2, 102610));
    expect(release.name, '26h2-102610');
  });

  test('a stamped build only updates when the stamp moved', () async {
    final service = UpdateService(
      client: _FakeClient(
        (_) async => _jsonResponse([
          {'tag_name': '26h2-102610', 'name': '26h2-102610', 'body': 'n'},
        ]),
      ),
    );

    expect(await service.checkForUpdate('26h2-102610'), isNull);
    expect(await service.checkForUpdate('26h2-102609'), isNotNull);
  });

  test('refuses an unreadable running version instead of guessing', () async {
    final service = UpdateService(
      client: _FakeClient((_) async => _jsonResponse(const [])),
    );

    expect(
      () => service.checkForUpdate('nightly'),
      throwsA(isA<UpdateCheckException>()),
    );
  });

  test('a request that hangs fails on a deadline instead of never', () async {
    // What api.github.com looks like from a network that drops packets: the
    // request neither succeeds nor fails, and without a deadline the check would
    // sit there. The response below arrives far too late to matter.
    final service = UpdateService(
      client: _FakeClient(
        (_) => Future<http.Response>.delayed(
          const Duration(seconds: 5),
          () => http.Response('{}', 200),
        ),
      ),
      timeout: const Duration(milliseconds: 50),
    );

    await expectLater(
      service.checkForUpdate('1.0.1'),
      throwsA(
        isA<UpdateCheckException>().having(
          (error) => error.message,
          'message',
          contains('超时'),
        ),
      ),
    );
  });

  test('reports a refusal as a failure, not as up to date', () async {
    // Unauthenticated GitHub requests are capped at 60 an hour per address.
    final rateLimited = UpdateService(
      client: _FakeClient(
        (_) async => http.Response('{"message":"API rate limit exceeded"}', 403),
      ),
    );
    expect(
      () => rateLimited.checkForUpdate('1.0.1'),
      throwsA(isA<UpdateCheckException>()),
    );

    final unreachable = UpdateService(
      client: _FakeClient((_) async => throw const SocketExceptionStub()),
    );
    expect(
      () => unreachable.checkForUpdate('1.0.1'),
      throwsA(isA<UpdateCheckException>()),
    );
  });
}

class SocketExceptionStub implements Exception {
  const SocketExceptionStub();
  @override
  String toString() => 'SocketException: connection refused';
}

http.Response _jsonResponse(Object? body) => http.Response(
      jsonEncode(body),
      200,
      headers: {'content-type': 'application/json; charset=utf-8'},
    );

class _FakeClient extends http.BaseClient {
  _FakeClient(this._handler);

  final Future<http.Response> Function(http.BaseRequest request) _handler;

  @override
  Future<http.StreamedResponse> send(http.BaseRequest request) async {
    final response = await _handler(request);
    return http.StreamedResponse(
      Stream<List<int>>.value(response.bodyBytes),
      response.statusCode,
      headers: response.headers,
      request: request,
    );
  }
}
