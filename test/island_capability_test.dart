import 'package:flutter_test/flutter_test.dart';
import 'package:techpie/services/island_service.dart';

void main() {
  IslandCapability capability({
    required String backend,
    bool islandLikely = false,
    bool islandGranted = false,
    bool liveUpdateGranted = false,
  }) =>
      IslandCapability.fromMap({
        'backend': backend,
        'postNotifications': true,
        'islandLikely': islandLikely,
        'islandGranted': islandGranted,
        'liveUpdateGranted': liveUpdateGranted,
      })!;

  test('HyperOS label requires backend, device support, and grant to agree', () {
    expect(
      capability(
        backend: 'hyper_island',
        islandLikely: true,
        islandGranted: true,
      ).displayBackend,
      'hyper_island',
    );
    expect(
      capability(backend: 'hyper_island', islandGranted: true).displayBackend,
      'plain',
    );
    expect(
      capability(backend: 'hyper_island', islandLikely: true).displayBackend,
      'plain',
    );
  });

  test('live updates and plain notifications keep their own labels', () {
    expect(
      capability(backend: 'live_update', liveUpdateGranted: true).displayBackend,
      'live_update',
    );
    expect(capability(backend: 'plain').displayBackend, 'plain');
  });
}
