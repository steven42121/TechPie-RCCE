import 'package:flutter/material.dart';

import '../../../models/course.dart';
import '../domain/glance_snapshot.dart';
import 'glance_formatters.dart';
import 'glance_widgets.dart';

class CoverCoursesPage extends StatelessWidget {
  const CoverCoursesPage({
    super.key,
    required this.snapshot,
    this.onRetry,
  });

  final GlanceSnapshot snapshot;
  final VoidCallback? onRetry;

  @override
  Widget build(BuildContext context) {
    final unavailable = glanceUnavailableState(
      snapshot,
      courses: true,
      compact: false,
      onRetry: onRetry,
    );
    if (unavailable != null) return unavailable;

    final lead = snapshot.leadCourse;
    if (lead == null) {
      return GlanceMessage(
        icon: Icons.nights_stay_outlined,
        title: '今天的课已结束',
        subtitle: '今天共 ${snapshot.courses.length} 节课',
      );
    }

    final rest = <GlanceCourse>[];
    var skippedLead = false;
    for (final course in snapshot.activeCourses) {
      if (!skippedLead && identical(course, lead)) {
        skippedLead = true;
        continue;
      }
      rest.add(course);
    }

    return ListView(
      padding: const EdgeInsets.fromLTRB(14, 4, 14, 8),
      children: [
        _LeadCourseCard(course: lead),
        if (rest.isNotEmpty) ...[
          const SizedBox(height: 12),
          Text(
            '之后',
            style: TextStyle(
              fontSize: 11,
              fontWeight: FontWeight.w600,
              color: Theme.of(context).colorScheme.onSurfaceVariant,
            ),
          ),
          const SizedBox(height: 6),
          for (final course in rest.take(4))
            Padding(
              padding: const EdgeInsets.only(bottom: 6),
              child: _CourseRow(course: course),
            ),
          if (rest.length > 4)
            Text(
              '还有 ${rest.length - 4} 节课',
              style: TextStyle(
                fontSize: 10,
                color: Theme.of(context).colorScheme.onSurfaceVariant,
              ),
            ),
        ],
      ],
    );
  }
}

class _LeadCourseCard extends StatelessWidget {
  const _LeadCourseCard({required this.course});

  final GlanceCourse course;

  @override
  Widget build(BuildContext context) {
    final scheme = Theme.of(context).colorScheme;
    final background = course.course.color.containerColor(scheme);
    final foreground = course.course.color.onContainerColor(scheme);
    return Container(
      padding: const EdgeInsets.fromLTRB(14, 12, 14, 13),
      decoration: BoxDecoration(
        color: background,
        borderRadius: BorderRadius.circular(16),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 7, vertical: 2),
                decoration: BoxDecoration(
                  color: foreground.withValues(alpha: 0.12),
                  borderRadius: BorderRadius.circular(999),
                ),
                child: Text(
                  _stateLabel(course.state),
                  style: TextStyle(
                    fontSize: 10,
                    fontWeight: FontWeight.w600,
                    color: foreground,
                  ),
                ),
              ),
              const SizedBox(width: 6),
              Expanded(
                child: Text(
                  formatGlanceCourseTime(course.course),
                  textAlign: TextAlign.end,
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                  style: TextStyle(
                    fontSize: 11,
                    fontWeight: FontWeight.w500,
                    color: foreground.withValues(alpha: 0.75),
                  ),
                ),
              ),
            ],
          ),
          const SizedBox(height: 8),
          Text(
            course.course.name,
            maxLines: 2,
            overflow: TextOverflow.ellipsis,
            style: TextStyle(
              fontSize: 19,
              height: 1.15,
              fontWeight: FontWeight.w700,
              color: foreground,
            ),
          ),
          const SizedBox(height: 5),
          Text(
            course.course.location.isEmpty ? '地点待定' : course.course.location,
            maxLines: 1,
            overflow: TextOverflow.ellipsis,
            style: TextStyle(
              fontSize: 11,
              color: foreground.withValues(alpha: 0.8),
            ),
          ),
        ],
      ),
    );
  }
}

class _CourseRow extends StatelessWidget {
  const _CourseRow({required this.course});

  final GlanceCourse course;

  @override
  Widget build(BuildContext context) {
    final scheme = Theme.of(context).colorScheme;
    return Container(
      padding: const EdgeInsets.fromLTRB(9, 8, 10, 8),
      decoration: BoxDecoration(
        color: scheme.surfaceContainerHighest.withValues(alpha: 0.55),
        borderRadius: BorderRadius.circular(11),
      ),
      child: Row(
        children: [
          Container(
            width: 3,
            height: 32,
            decoration: BoxDecoration(
              color: course.course.color.containerColor(scheme),
              borderRadius: BorderRadius.circular(3),
            ),
          ),
          const SizedBox(width: 9),
          SizedBox(
            width: 42,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                FittedBox(
                  fit: BoxFit.scaleDown,
                  alignment: Alignment.centerLeft,
                  child: Text(
                    course.course.startTime,
                    style: TextStyle(
                      fontSize: 11,
                      fontWeight: FontWeight.w600,
                      color: scheme.onSurface,
                    ),
                  ),
                ),
                FittedBox(
                  fit: BoxFit.scaleDown,
                  alignment: Alignment.centerLeft,
                  child: Text(
                    course.course.endTime,
                    style: TextStyle(
                      fontSize: 10,
                      color: scheme.onSurfaceVariant,
                    ),
                  ),
                ),
              ],
            ),
          ),
          const SizedBox(width: 6),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  course.course.name,
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                  style: TextStyle(
                    fontSize: 13,
                    fontWeight: FontWeight.w600,
                    color: scheme.onSurface,
                  ),
                ),
                if (course.course.location.isNotEmpty)
                  Text(
                    course.course.location,
                    maxLines: 1,
                    overflow: TextOverflow.ellipsis,
                    style: TextStyle(
                      fontSize: 10,
                      color: scheme.onSurfaceVariant,
                    ),
                  ),
              ],
            ),
          ),
          const SizedBox(width: 4),
          Text(
            _shortStateLabel(course.state),
            style: TextStyle(
              fontSize: 10,
              color: scheme.onSurfaceVariant,
            ),
          ),
        ],
      ),
    );
  }
}

class CoverAssignmentsPage extends StatelessWidget {
  const CoverAssignmentsPage({
    super.key,
    required this.snapshot,
    this.onRetry,
  });

  final GlanceSnapshot snapshot;
  final VoidCallback? onRetry;

  @override
  Widget build(BuildContext context) {
    final unavailable = glanceUnavailableState(
      snapshot,
      courses: false,
      compact: false,
      onRetry: onRetry,
    );
    if (unavailable != null) return unavailable;

    final items = snapshot.pendingAssignments.take(3).toList();
    return ListView(
      padding: const EdgeInsets.fromLTRB(14, 4, 14, 8),
      children: [
        for (final item in items)
          Padding(
            padding: const EdgeInsets.only(bottom: 7),
            child: _AssignmentCard(item: item, now: snapshot.now),
          ),
        if (snapshot.pendingAssignments.length > items.length)
          Text(
            '还有 ${snapshot.pendingAssignments.length - items.length} 项待办',
            style: TextStyle(
              fontSize: 10,
              color: Theme.of(context).colorScheme.onSurfaceVariant,
            ),
          ),
      ],
    );
  }
}

class _AssignmentCard extends StatelessWidget {
  const _AssignmentCard({required this.item, required this.now});

  final GlanceAssignment item;
  final DateTime now;

  @override
  Widget build(BuildContext context) {
    final scheme = Theme.of(context).colorScheme;
    final urgency = _urgencyColor(item.assignment.due, now, scheme);
    final course = item.assignment.course;
    return Container(
      padding: const EdgeInsets.fromLTRB(10, 9, 10, 9),
      decoration: BoxDecoration(
        color: scheme.surfaceContainerHighest.withValues(alpha: 0.55),
        borderRadius: BorderRadius.circular(12),
      ),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
            width: 3,
            height: 34,
            margin: const EdgeInsets.only(top: 1),
            decoration: BoxDecoration(
              color: urgency,
              borderRadius: BorderRadius.circular(3),
            ),
          ),
          const SizedBox(width: 9),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  item.assignment.title,
                  maxLines: 2,
                  overflow: TextOverflow.ellipsis,
                  style: TextStyle(
                    fontSize: 13,
                    height: 1.25,
                    fontWeight: FontWeight.w600,
                    color: scheme.onSurface,
                  ),
                ),
                const SizedBox(height: 4),
                Text(
                  course.isEmpty
                      ? item.assignment.kind.label
                      : '$course · ${item.assignment.kind.label}',
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                  style: TextStyle(
                    fontSize: 10,
                    color: scheme.onSurfaceVariant,
                  ),
                ),
                const SizedBox(height: 2),
                Text(
                  item.dueLabel,
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                  style: TextStyle(
                    fontSize: 11,
                    fontWeight: FontWeight.w600,
                    color: urgency,
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

String _stateLabel(GlanceCourseState state) => switch (state) {
      GlanceCourseState.ongoing => '正在上课',
      GlanceCourseState.soon => '即将开始',
      GlanceCourseState.upcoming => '下一节',
      GlanceCourseState.past => '已结束',
    };

String _shortStateLabel(GlanceCourseState state) => switch (state) {
      GlanceCourseState.ongoing => '进行中',
      GlanceCourseState.soon => '即将',
      GlanceCourseState.upcoming => '待开始',
      GlanceCourseState.past => '已结束',
    };

Color _urgencyColor(DateTime due, DateTime now, ColorScheme scheme) {
  final today = DateTime(now.year, now.month, now.day);
  final target = DateTime(due.year, due.month, due.day);
  final days = target.difference(today).inDays;
  if (days <= 0) return scheme.error;
  if (days == 1) return scheme.tertiary;
  return scheme.onSurfaceVariant;
}
