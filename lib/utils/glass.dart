import 'dart:ui' show Brightness, ImageFilter;

import 'package:flutter/painting.dart';

/// Telegram's blur3 glass recipe, ported from
/// `BlurredBackgroundProviderImpl.mainTabs()` and
/// `DownscaleScrollableNoiseSuppressor` (study: ~/repos/Telegram/UI-STUDY,
/// glass-main-tabs.md §5).
abstract final class TgGlass {
  /// Overlay alpha for the plain frosted recipe. Liquid glass deliberately
  /// runs a lighter fill (see `GlassTheme.overlayAlpha`) so the blur and the
  /// content colour read through it — its bevel and sheen carry the glass.
  static const double overlayAlpha = 0.76;

  /// The blurred backdrop gets its saturation tripled so content color
  /// survives the heavy blur.
  static const double saturationBoost = 3.0;

  /// Hairline stroke: 0.4dp, top and bottom colors differ (a soft "light
  /// from above" bevel). Light themes use black micro-strokes, dark themes
  /// white micro-highlights.
  static const double strokeWidth = 0.4;

  /// Liquid glass rims its surfaces with a much brighter bevel, so the mode
  /// is unmistakable at a glance instead of only over busy content.
  static const double liquidStrokeWidth = 1.2;

  static const Color _liquidStrokeLight = Color(0x8FFFFFFF); // ~0.56 white
  static const Color _liquidStrokeDark = Color(0x59FFFFFF); // ~0.35 white
  static const double shadowBlur = 2.667;
  static const double shadowDy = 0.85;

  static Color strokeTop(Brightness brightness) =>
      brightness == Brightness.light ? const Color(0x11000000) : const Color(0x06FFFFFF);

  static Color strokeBottom(Brightness brightness) =>
      brightness == Brightness.light ? const Color(0x20000000) : const Color(0x11FFFFFF);

  static Color liquidStrokeTop(Brightness brightness) =>
      brightness == Brightness.light ? _liquidStrokeLight : _liquidStrokeDark;

  static Color liquidStrokeBottom(Brightness brightness) =>
      brightness == Brightness.light ? const Color(0x33FFFFFF) : const Color(0x24FFFFFF);

  static Color shadowColor(Brightness brightness) =>
      brightness == Brightness.light ? const Color(0x20000000) : const Color(0x04FFFFFF);

  /// Liquid-glass specular sheen drawn over the surface: a strong top-left
  /// highlight that fades out toward the bottom-right, reading as gloss on
  /// clear glass rather than a barely-there tint.
  static Gradient sheen(Brightness brightness) {
    final peak = brightness == Brightness.light ? 0.32 : 0.20;
    return LinearGradient(
      begin: Alignment.topLeft,
      end: Alignment.bottomRight,
      colors: [
        const Color(0xFFFFFFFF).withValues(alpha: peak),
        const Color(0xFFFFFFFF).withValues(alpha: peak * 0.15),
      ],
    );
  }

  /// Inverse src-over solve (`solveSrcColor`, the heart of the recipe):
  /// given the color [behind] the glass and the [target] color the glass
  /// should *end up looking like*, return the translucent overlay to draw —
  /// `src = (target - behind*(1-alpha)) / alpha` per channel. The designer
  /// declares the final color; the overlay is derived, so the glass lands
  /// exactly on the theme color instead of "background × opacity" luck.
  static Color solveSrcColor(
    Color behind,
    Color target, {
    double alpha = overlayAlpha,
  }) {
    int solve(double t, double b) => ((t - b * (1 - alpha)) / alpha * 255).round().clamp(0, 255);
    return Color.fromARGB(
      (alpha * 255).round(),
      solve(target.r, behind.r),
      solve(target.g, behind.g),
      solve(target.b, behind.b),
    );
  }

  /// Backdrop filter: gaussian blur then saturation ×3, matching the
  /// original's RenderEffect chain (blur radius ≈40dp at 8× downsample —
  /// a sigma in the low twenties at full resolution).
  static ImageFilter backdrop({double sigma = 24}) => ImageFilter.compose(
        outer: const ColorFilter.matrix(_saturation3),
        inner: ImageFilter.blur(sigmaX: sigma, sigmaY: sigma),
      );

  // Luminance-weighted saturation matrix for s = 3:
  // sr = (1-s)*0.213, sg = (1-s)*0.715, sb = (1-s)*0.072.
  static const List<double> _saturation3 = [
    2.574, -1.430, -0.144, 0, 0, //
    -0.426, 1.570, -0.144, 0, 0, //
    -0.426, -1.430, 2.856, 0, 0, //
    0, 0, 0, 1, 0,
  ];
}
