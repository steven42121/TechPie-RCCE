import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:techpie/services/theme_service.dart';
import 'package:techpie/widgets/tg_glass_surface.dart';

void main() {
  testWidgets('liquid fill and stroke span the same constrained card width',
      (tester) async {
    await tester.pumpWidget(
      MaterialApp(
        theme: ThemeData(
          extensions: const [GlassTheme(AppGlassMode.liquid)],
        ),
        home: const Scaffold(
          body: Align(
            alignment: Alignment.topLeft,
            child: SizedBox(
              width: 400,
              child: TgGlassCard(
                child: Padding(
                  padding: EdgeInsets.all(24),
                  child: Text('Welcome'),
                ),
              ),
            ),
          ),
        ),
      ),
    );
    final surface = find.byType(TgGlassSurface);
    final stroke = find.descendant(
      of: surface,
      matching: find.byWidgetPredicate(
        (widget) => widget is CustomPaint && widget.foregroundPainter != null,
      ),
    );
    expect(tester.getSize(surface).width, 400);
    expect(tester.getSize(stroke.first).width, 400);
    expect(tester.getTopLeft(stroke.first), tester.getTopLeft(surface));
    expect(tester.takeException(), isNull);
  });

  testWidgets('a card only turns to glass in Full Liquid Glass', (tester) async {
    Future<GlassTheme> cardsFor(AppGlassMode mode) async {
      GlassTheme? resolved;
      await tester.pumpWidget(
        MaterialApp(
          theme: ThemeData(extensions: [GlassTheme(mode)]),
          // AnimatedTheme would hand back the previous mode at t = 0.
          themeAnimationDuration: Duration.zero,
          home: Builder(
            builder: (context) {
              resolved = GlassTheme.cardsOf(context);
              return const SizedBox();
            },
          ),
        ),
      );
      return resolved!;
    }

    expect((await cardsFor(AppGlassMode.liquid)).mode, AppGlassMode.classic);
    expect((await cardsFor(AppGlassMode.frosted)).mode, AppGlassMode.classic);
    expect(
      (await cardsFor(AppGlassMode.fullLiquid)).mode,
      AppGlassMode.fullLiquid,
    );
  });
}
