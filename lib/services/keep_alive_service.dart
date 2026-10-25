import 'package:flutter/services.dart';

import '../utils/platform.dart';
import 'storage_service.dart';

/// The 保活 switch: an opt-in resident process, so the island and the band
/// reminders keep working while the app is not in front.
///
/// Off by default — the host keeps no process alive until the user asks for
/// one — and it costs a persistent notification, which Android requires of any
/// foreground service.
final class KeepAliveService {
  KeepAliveService(this._storage, {MethodChannel? channel})
      : _channel = channel ?? const MethodChannel('techpie/keep_alive');

  final StorageService _storage;
  final MethodChannel _channel;

  bool get supported => isAndroid();

  bool get enabled => supported && _storage.keepAliveEnabled;

  /// Persist the choice and apply it to the host right away.
  Future<void> setEnabled(bool value) async {
    await _storage.setKeepAliveEnabled(value);
    await _apply(value);
  }

  /// Apply whatever is stored: the boot path, where nothing has changed.
  Future<void> sync() => _apply(enabled);

  Future<void> _apply(bool value) async {
    try {
      await _channel.invokeMethod<bool>('setEnabled', {'value': value});
    } on PlatformException {
      // The switch stays meaningful even when the host cannot start a service.
    } on MissingPluginException {
      // Desktop and iOS keep no resident process for this.
    }
  }
}
