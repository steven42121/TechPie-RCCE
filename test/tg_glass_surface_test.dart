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
}
