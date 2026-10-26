import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import '../models/screen_form_factor.dart';
import '../utils/platform.dart';

/// Reports the display the activity is on and classifies it as a small surface.
///
/// The channel is Android-only; every other platform simply reports [none].
class ScreenFormFactorService extends ChangeNotifier {
  ScreenFormFactorService({
    Future<ScreenFormFactorInfo?> Function()? loader,
    bool? androidOverride,
  })  : _loader = loader ?? _loadFromChannel,
        _android = androidOverride ?? isAndroid();

  static const MethodChannel _channel = MethodChannel(
    'techpie/screen_form_factor',
  );

  final Future<ScreenFormFactorInfo?> Function() _loader;
  final bool _android;

  ScreenFormFactorInfo? _info;
  bool _inFlight = false;
  bool _refreshQueued = false;
  bool _disposed = false;

  ScreenFormFactorInfo? get info => _info;

  bool get supported => _android;

  Future<void> initialize() => refresh();

  Future<void> refresh() async {
    if (!_android || _disposed) return;
    if (_inFlight) {
      _refreshQueued = true;
      return;
    }
    _inFlight = true;
    try {
      final next = await _loadWithRetry();
      if (_disposed || next == null || next == _info) return;
      _info = next;
      notifyListeners();
    } finally {
      _inFlight = false;
      if (_refreshQueued && !_disposed) {
        _refreshQueued = false;
        unawaited(refresh());
      }
    }
  }

  Future<ScreenFormFactorInfo?> _loadWithRetry() async {
    for (var attempt = 0; attempt < 2; attempt++) {
      try {
        return await _loader().timeout(const Duration(seconds: 2));
      } on MissingPluginException {
        if (attempt == 1) return null;
        await Future<void>.delayed(const Duration(milliseconds: 150));
      } on TimeoutException {
        return null;
      } catch (_) {
        return null;
      }
    }
    return null;
  }

  static Future<ScreenFormFactorInfo?> _loadFromChannel() async {
    final map = await _channel.invokeMapMethod<Object?, Object?>('info');
    if (map == null) return null;
    return ScreenFormFactorInfo.fromMap(map);
  }

  @override
  void dispose() {
    _disposed = true;
    super.dispose();
  }
}

/// Pure classification, kept free of platform checks so it can be unit-tested.
///
/// - The rear screen is a secondary display (`displayId != 0`). The size
///   fallback covers HyperOS builds where the reflection probe is unavailable.
/// - The MIX Flip cover screen shares display 0 with the inner screen, so the
///   window size is the signal: the app area is ~294dp wide and taller than it
///   is wide. The inner screen is ~445dp wide; a split-screen window on it is
///   still excluded by the width/orientation pair unless it is extremely narrow.
GlanceSurface classifyGlanceSurface({
  required Size logicalSize,
  required ScreenFormFactorInfo? info,
  required bool enabled,
}) {
  if (!enabled || info == null) return GlanceSurface.none;
  final width = logicalSize.width;
  final height = logicalSize.height;
  if (width <= 0 || height <= 0) return GlanceSurface.none;

  final secondary = info.displayId != 0;
  if (info.isRearDevice && secondary) return GlanceSurface.rear;
  if (secondary && width > height && height <= 320) return GlanceSurface.rear;
  if (info.isFlipDevice && width <= 380 && height > width) {
    return GlanceSurface.cover;
  }
  return GlanceSurface.none;
}
