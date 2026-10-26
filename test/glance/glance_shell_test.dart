import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:techpie/features/glance/domain/glance_snapshot.dart';
import 'package:techpie/features/glance/presentation/glance_shell.dart';
import 'package:techpie/models/assignment.dart';
import 'package:techpie/models/course.dart';
import 'package:techpie/models/screen_form_factor.dart';
import 'package:techpie/services/glance_settings_service.dart';

final _now = DateTime(2026, 10, 26, 10, 0);

Course _course(String name, String start, String end, CourseColor color) {
  return Course(
    name: name,
    location: 'A101',
    dayOfWeek: _now.weekday,
    color: color,
    placement: CoursePlacement(
      startPeriod: 1,
      endPeriod: 1,
      gridStart: 0,
      gridEnd: 1,
      usesCustomTime: true,
      startTime: start,
      endTime: end,
      label: '$start\n$end',
    ),
  );
}

GlanceSnapshot _snapshot({
  bool loggedIn = true,
  List<GlanceCourse>? courses,
  List<GlanceAssignment>? assignments,
}) {
  return GlanceSnapshot(
    now: _now,
    week: 9,
    inTerm: true,
    loggedIn: loggedIn,
    courses: courses ??
        [
          GlanceCourse(
            course: _course('高等数学', '10:00', '10:45', CourseColor.primary),
            state: GlanceCourseState.ongoing,
          ),
          GlanceCourse(
            course: _course('大学物理', '14:00', '14:45', CourseColor.secondary),
            state: GlanceCourseState.upcoming,
          ),
        ],
    pendingAssignments: assignments ??
        [
          GlanceAssignment(
            assignment: Assignment(
              id: 'a1',
              platform: 'test',
              title: '提交实验报告',
              course: '大学物理',
              due: _now.add(const Duration(days: 1)),
            ),
            dueLabel: '明天 23:59',
          ),
        ],
    scheduleLoading: false,
    scheduleError: null,
    assignmentsLoading: false,
    assignmentsError: null,
  );
}

Future<void> _pumpGlance(
  WidgetTester tester, {
  required Size size,
  required GlanceSurface surface,
  required GlanceSnapshot snapshot,
  double textScale = 1,
}) async {
  tester.view.physicalSize = size;
  tester.view.devicePixelRatio = 1;
  tester.platformDispatcher.textScaleFactorTestValue = textScale;
  addTearDown(tester.view.resetPhysicalSize);
  addTearDown(tester.view.resetDevicePixelRatio);
  addTearDown(tester.platformDispatcher.clearTextScaleFactorTestValue);

  await tester.pumpWidget(
    MaterialApp(
      theme: ThemeData(useMaterial3: true),
      darkTheme: ThemeData(useMaterial3: true, brightness: Brightness.dark),
      home: GlanceShell(
        surface: surface,
        snapshot: snapshot,
        sections: GlanceSection.values,
        paymentBuilder: (_) => const Center(child: Text('支付预览')),
      ),
    ),
  );
  await tester.pumpAndSettle();
}

void main() {
  testWidgets('cover layout renders course, deadlines and payment tabs', (
    tester,
  ) async {
    await _pumpGlance(
      tester,
      size: const Size(294, 506),
      surface: GlanceSurface.cover,
      snapshot: _snapshot(),
    );

    expect(find.text('高等数学'), findsOneWidget);
    expect(find.text('正在上课'), findsOneWidget);
    expect(find.text('之后'), findsOneWidget);
    expect(find.text('大学物理'), findsOneWidget);

    await tester.tap(find.text('作业'));
    await tester.pumpAndSettle();
    expect(find.text('提交实验报告'), findsOneWidget);
    expect(find.text('明天 23:59'), findsOneWidget);

    await tester.tap(find.text('消费码'));
    await tester.pumpAndSettle();
    expect(find.text('支付预览'), findsOneWidget);
    expect(tester.takeException(), isNull);
  });

  testWidgets('cover layout survives 1.5x text scaling', (tester) async {
    await _pumpGlance(
      tester,
      size: const Size(294, 506),
      surface: GlanceSurface.cover,
      snapshot: _snapshot(),
      textScale: 1.5,
    );

    expect(find.text('高等数学'), findsOneWidget);
    expect(tester.takeException(), isNull);
  });

  testWidgets('rear layout is compact and fits its short panel', (tester) async {
    await _pumpGlance(
      tester,
      size: const Size(340, 210),
      surface: GlanceSurface.rear,
      snapshot: _snapshot(),
    );

    expect(find.text('高等数学'), findsOneWidget);
    expect(find.text('之后 1 节'), findsOneWidget);
    expect(tester.takeException(), isNull);

    await tester.tap(find.text('作业'));
    await tester.pumpAndSettle();
    expect(find.text('提交实验报告'), findsOneWidget);
    expect(tester.takeException(), isNull);

    await tester.tap(find.text('消费码'));
    await tester.pumpAndSettle();
    expect(find.text('支付预览'), findsOneWidget);
    expect(tester.takeException(), isNull);
  });

  testWidgets('rear layout survives 1.5x text scaling', (tester) async {
    await _pumpGlance(
      tester,
      size: const Size(340, 210),
      surface: GlanceSurface.rear,
      snapshot: _snapshot(),
      textScale: 1.5,
    );

    expect(find.text('高等数学'), findsOneWidget);
    expect(tester.takeException(), isNull);
  });

  testWidgets('empty and logged-out states are explicit', (tester) async {
    await _pumpGlance(
      tester,
      size: const Size(294, 506),
      surface: GlanceSurface.cover,
      snapshot: _snapshot(courses: const [], assignments: const []),
    );
    expect(find.text('今天没有课程'), findsOneWidget);

    await tester.tap(find.text('作业'));
    await tester.pumpAndSettle();
    expect(find.text('没有待办事项'), findsOneWidget);

    await tester.pumpWidget(const SizedBox.shrink());
    await _pumpGlance(
      tester,
      size: const Size(294, 506),
      surface: GlanceSurface.cover,
      snapshot: _snapshot(
        loggedIn: false,
        courses: const [],
        assignments: const [],
      ),
    );
    expect(find.text('请先在完整 App 登录'), findsOneWidget);
    expect(tester.takeException(), isNull);
  });

  testWidgets('a finished day says so instead of showing an empty list', (
    tester,
  ) async {
    final past = GlanceCourse(
      course: _course('早课', '08:00', '08:45', CourseColor.primary),
      state: GlanceCourseState.past,
    );
    await _pumpGlance(
      tester,
      size: const Size(294, 506),
      surface: GlanceSurface.cover,
      snapshot: _snapshot(courses: [past], assignments: const []),
    );

    expect(find.text('今天的课已结束'), findsOneWidget);
    expect(tester.takeException(), isNull);
  });
}
