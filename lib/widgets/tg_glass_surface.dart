import 'package:flutter/material.dart';

import '../services/theme_service.dart';
import '../utils/glass.dart';

/// A rounded surface painted with the Telegram glass recipe shared by the
/// bottom selection bar and the welcome card.
///
/// The fill is solved from the theme's surface so the glass lands exactly on
/// the same target tone as the bottom bar, the backdrop gets the studied
/// blur + saturation pass, and `liquid` adds the specular sheen and the bright
/// bevel. `classic` draws an opaque fill with the material hairline outline.
class TgGlassSurface extends StatelessWidget {
  const TgGlassSurface({
    super.key,
    required this.radius,
    required this.child,
    this.height,
    this.padding = EdgeInsets.zero,
    this.card = false,
  });

  final double radius;
  final double? height;
  final EdgeInsetsGeometry padding;
  final Widget child;

  /// Content cards follow [GlassTheme.cardsOf] — glass only in
  /// `Full Liquid Glass`, an opaque surface otherwise — and sample the backdrop
  /// without the shared group snapshot, which is what kept their colour
  /// drifting while the page scrolled.
  final bool card;

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final scheme = theme.colorScheme;
    final brightness = theme.brightness;
    final glass = card ? GlassTheme.cardsOf(context) : GlassTheme.of(context);
    final borderRadius = BorderRadius.circular(radius);

    // Same target tone as the bottom bar: light glass reads as the lowest
    // surface container, dark glass as the plain container tone.
    final fillTarget = brightness == Brightness.light
        ? scheme.surfaceContainerLowest
        : scheme.surfaceContainer;
    final fill = glass.translucent
        ? TgGlass.solveSrcColor(
            scheme.surface,
            fillTarget,
            alpha: glass.overlayAlpha,
          )
        : fillTarget;

    final strokeTop = glass.sheen
        ? TgGlass.liquidStrokeTop(brightness)
        : glass.translucent
            ? TgGlass.strokeTop(brightness)
            : scheme.outlineVariant;
    final strokeBottom = glass.sheen
        ? TgGlass.liquidStrokeBottom(brightness)
        : glass.translucent
            ? TgGlass.strokeBottom(brightness)
            : scheme.outlineVariant;
    final strokeWidth =
        glass.sheen ? TgGlass.liquidStrokeWidth : TgGlass.strokeWidth;

    return RepaintBoundary(
      child: DecoratedBox(
      decoration: BoxDecoration(
        borderRadius: borderRadius,
        boxShadow: [
          BoxShadow(
            color: TgGlass.shadowColor(brightness),
            blurRadius: TgGlass.shadowBlur,
            offset: const Offset(0, TgGlass.shadowDy),
          ),
        ],
      ),
      child: ClipRRect(
        borderRadius: borderRadius,
        child: _GlassComposite(
          glass: glass,
          brightness: brightness,
          grouped: !card,
          child: CustomPaint(
            foregroundPainter: _GlassStrokePainter(
              top: strokeTop,
              bottom: strokeBottom,
              radius: radius,
              strokeWidth: strokeWidth,
            ),
            child: SizedBox(
              height: height,
              child: DecoratedBox(
                decoration: BoxDecoration(
                  color: fill,
                  borderRadius: borderRadius,
                ),
                child: Padding(padding: padding, child: child),
              ),
            ),
          ),
        ),
      ),
      ),
    );
  }
}

/// Composites the surface over the content behind it: a blur in the
/// translucent modes — with the saturation boost and a specular sheen on top
/// in liquid mode — and plain paint in `classic`.
class _GlassComposite extends StatelessWidget {
  const _GlassComposite({
    required this.glass,
    required this.brightness,
    required this.child,
    this.grouped = true,
  });

  final GlassTheme glass;
  final Brightness brightness;
  final Widget child;
  final bool grouped;

  @override
  Widget build(BuildContext context) {
    if (!glass.translucent) return child;

    // The floating bar shares one backdrop snapshot; a card inside a scrolling
    // list must not, or the blur it reuses is from wherever the group was
    // captured and the surface colour drifts as the page moves.
    final blurred = grouped
        ? BackdropFilter.grouped(filter: TgGlass.backdrop(), child: child)
        : BackdropFilter(filter: TgGlass.backdrop(), child: child);
    if (!glass.sheen) return blurred;

    return Stack(
      fit: StackFit.passthrough,
      children: [
        blurred,
        Positioned.fill(
          child: IgnorePointer(
            child: DecoratedBox(
              decoration: BoxDecoration(
                gradient: TgGlass.sheen(brightness),
              ),
            ),
          ),
        ),
      ],
    );
  }
}

/// The glass bevel: one stroked rounded rectangle whose color runs from the
/// top hairline to the bottom hairline.
class _GlassStrokePainter extends CustomPainter {
  const _GlassStrokePainter({
    required this.top,
    required this.bottom,
    required this.radius,
    this.strokeWidth = TgGlass.strokeWidth,
  });

  final Color top;
  final Color bottom;
  final double radius;
  final double strokeWidth;

  @override
  void paint(Canvas canvas, Size size) {
    // The stroke rides the surface edge: inset by half its width and keep the
    // corner arcs concentric with the fill (radius shrinks by the same inset),
    // so the bevel hugs the rounded corners instead of drifting off them.
    final inset = strokeWidth / 2;
    final rect = (Offset.zero & size).deflate(inset);
    final corner = (radius - inset).clamp(0.0, double.infinity);
    final paint = Paint()
      ..style = PaintingStyle.stroke
      ..strokeWidth = strokeWidth
      ..shader = LinearGradient(
        begin: Alignment.topCenter,
        end: Alignment.bottomCenter,
        colors: [top, bottom],
      ).createShader(rect);
    canvas.drawRRect(
      RRect.fromRectAndRadius(rect, Radius.circular(corner)),
      paint,
    );
  }

  @override
  bool shouldRepaint(covariant _GlassStrokePainter old) =>
      old.top != top ||
      old.bottom != bottom ||
      old.radius != radius ||
      old.strokeWidth != strokeWidth;
}

/// A card-shaped [TgGlassSurface] with a transparent [Material] inside so
/// list-tile ink effects paint above the glass instead of under its fill.
class TgGlassCard extends StatelessWidget {
  const TgGlassCard({super.key, this.radius = 12, required this.child});

  final double radius;
  final Widget child;

  @override
  Widget build(BuildContext context) => TgGlassSurface(
        radius: radius,
        card: true,
        child: Material(type: MaterialType.transparency, child: child),
      );
}
