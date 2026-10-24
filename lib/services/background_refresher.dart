import 'package:flutter/services.dart';

import 'storage_service.dart';

/// Asks the host to keep — or drop — the periodic wake-up behind the island
/// and the spending alerts. The host runs a worker every quarter hour and lets
/// the process die again: a killed app keeps no resident process, and the
/// reminders still arrive.
Future<void> syncBackgroundRefresh(StorageService storage) async {
  final wanted = storage.islandEnabled || storage.spendingAlertsEnabled;
  try {
    await const MethodChannel('techpie/background')
        .invokeMethod<bool>(wanted ? 'schedule' : 'cancel');
  } on PlatformException {
    // A host that cannot schedule still has the in-app refresh.
  } on MissingPluginException {
    // Desktop and iOS have no background host for this.
  }
}
