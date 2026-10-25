import 'dart:async';
import 'dart:convert';

import 'package:http/http.dart' as http;
import 'package:package_info_plus/package_info_plus.dart';

import '../utils/product_version.dart';

/// Thrown when the check cannot produce an answer. The message is user-facing,
/// so it says what happened in the user's terms rather than in HTTP's.
class UpdateCheckException implements Exception {
  UpdateCheckException(this.message);

  final String message;

  @override
  String toString() => message;
}

/// A published release that is newer than the running build.
class ReleaseInfo {
  const ReleaseInfo({
    required this.version,
    required this.name,
    required this.notes,
  });

  /// The version the release carries, comparable with the running one.
  final ProductVersion version;

  /// What the release calls itself — `v1.0.1`, `v1.0.1-rc.2`.
  final String name;

  /// The release's changelog, with the pipeline's checksum block removed.
  final String notes;

  @override
  String toString() => 'ReleaseInfo($name)';
}

/// Checks GitHub for a newer release than the one running.
///
/// Deliberately not part of the backend: the answer comes from the releases
/// page a user would look at anyway, so it stays correct even when the API is
/// down, and it needs no account.
class UpdateService {
  UpdateService({
    http.Client? client,
    // A route that drops packets instead of refusing them — which is what
    // api.github.com looks like from some networks — leaves the request hanging
    // rather than failing, so the check carries its own deadline. Injectable so
    // a test does not have to wait one out.
    this.timeout = const Duration(seconds: 12),
  }) : _client = client ?? http.Client();

  final http.Client _client;
  final Duration timeout;

  /// Where updates come from: the fork that publishes the test builds.
  static const repository = 'steven42121/TechPie-RCCE';

  /// Where the update button sends the user. The list page, not
  /// `releases/latest`: GitHub's "latest" never counts prereleases, and this
  /// fork ships its test builds as exactly those — so the redirect would miss
  /// the newest one, or 404 while only candidates are out.
  static const releasesPage = 'https://github.com/$repository/releases';

  static Uri get _releasesUri =>
      Uri.https('api.github.com', '/repos/$repository/releases');

  /// The version this build is, for a caller that has no other reason to hold it
  /// — the silent check has none. Null when the platform cannot say or the answer
  /// is not a version: a check against an unknown version would answer about
  /// nothing.
  static Future<ProductVersion?> currentProductVersion() async {
    try {
      final info = await PackageInfo.fromPlatform();
      final raw = info.buildNumber.isEmpty
          ? info.version
          : '${info.version}+${info.buildNumber}';
      return ProductVersion.tryParse(raw);
    } catch (_) {
      return null;
    }
  }

  /// The newest published release when it is newer than [current], or null when
  /// the running build is already the newest — which is not a failure, and must
  /// not read like one.
  Future<ReleaseInfo?> checkForUpdate(ProductVersion current) async {
    final http.Response response;
    try {
      response = await _client
          .get(
            _releasesUri,
            headers: {
              // Pin the API version this payload is read under, and say who is
              // asking: GitHub rejects a request with no user agent.
              'Accept': 'application/vnd.github+json',
              'X-GitHub-Api-Version': '2022-11-28',
              'User-Agent': 'TechPie/$current',
            },
          )
          .timeout(timeout);
    } on TimeoutException {
      throw UpdateCheckException('连接 GitHub 超时，请检查网络后重试。');
    } catch (error) {
      // The text here is what the user reads, and it is usually the only clue
      // about which of the network's ways of failing this was.
      throw UpdateCheckException('无法连接 GitHub：$error');
    }

    switch (response.statusCode) {
      case 200:
        break;
      case 404:
        // The repository is gone or renamed: nothing to check against.
        throw UpdateCheckException('GitHub 上还没有已发布的版本。');
      case 403:
        // Unauthenticated requests are capped at 60 an hour per address.
        throw UpdateCheckException('GitHub 暂时拒绝了请求（可能是请求过于频繁），请稍后再试。');
      default:
        throw UpdateCheckException('GitHub 返回了 HTTP ${response.statusCode}。');
    }

    final List<dynamic> payload;
    try {
      payload = jsonDecode(utf8.decode(response.bodyBytes)) as List<dynamic>;
    } catch (_) {
      throw UpdateCheckException('GitHub 的响应无法解析。');
    }

    // GitHub lists releases by publication date, not version order — and
    // unlike `releases/latest`, this view carries the candidates this fork
    // publishes as test builds. So the offer is the highest readable version
    // in the list, never merely the first entry; an entry without a readable
    // version (or a draft) is stepped over rather than failing the check for
    // every release behind it.
    ProductVersion? newest;
    var newestTag = '';
    var newestTitle = '';
    var newestBody = '';
    var firstTag = '';
    for (final entry in payload) {
      if (entry is! Map<String, dynamic> || entry['draft'] == true) {
        continue;
      }
      final tag = (entry['tag_name'] as String? ?? '').trim();
      if (firstTag.isEmpty) firstTag = tag;
      final title = (entry['name'] as String? ?? '').trim();
      final version = ProductVersion.tryParse(tag) ?? ProductVersion.tryParse(title);
      if (version == null) continue;
      if (newest == null || version.compareTo(newest) > 0) {
        newest = version;
        newestTag = tag;
        newestTitle = title;
        newestBody = entry['body'] as String? ?? '';
      }
    }

    if (payload.isEmpty) {
      // Nothing published yet: nothing to update to, and nothing broken.
      throw UpdateCheckException('GitHub 上还没有已发布的版本。');
    }
    if (newest == null) {
      // Better to say we cannot tell than to prompt for an update we do not
      // understand: a wrong prompt is worse than none.
      throw UpdateCheckException('无法识别 GitHub 上的版本号（$firstTag）。');
    }
    if (!newest.isNewerThan(current)) {
      return null;
    }

    // Two builds of one version differ only by `+B`, and the build number is not
    // part of the title (`v1.0.1`). Offering this update by title would read as
    // "still 1.0.1" to everyone the build is the whole difference — so the tag,
    // which carries `+B`, is what names it then.
    final sameVersion = newest.toString() == current.toString();
    return ReleaseInfo(
      version: newest,
      name: newestTitle.isNotEmpty && !sameVersion ? newestTitle : newestTag,
      notes: _changelog(newestBody),
    );
  }

  static final RegExp _checksumBlock = RegExp(r'\n#+ *SHA-256\b');

  /// `release.yml` writes the changelog and the checksums into one body. A
  /// checksum list is not a changelog, so the block is dropped before the text
  /// reaches a dialog.
  static String _changelog(String body) {
    final match = _checksumBlock.firstMatch(body);
    final changelog = (match == null ? body : body.substring(0, match.start)).trim();
    return changelog.isEmpty ? '（这个版本没有写更新说明。）' : changelog;
  }
}
