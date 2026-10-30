import 'package:flutter_test/flutter_test.dart';
import 'package:techpie/models/course.dart';
import 'package:techpie/pages/schedule_page.dart';

Course _courseEndingAt(int period) => Course(
      name: 'Course',
      location: '',
      dayOfWeek: 1,
      placement: CoursePlacement(
        startPeriod: period,
        endPeriod: period,
        gridStart: (period - 1).toDouble(),
        gridEnd: period.toDouble(),
        usesCustomTime: false,
        startTime: '08:15',
        endTime: '09:00',
        label: '',
      ),
    );

void main() {
  // The timetable used to draw all 13 periods no matter where the day ended,
  // which is most of a screen of empty grid under the last class.
  test('the table ends at the last period a course uses', () {
    expect(timetableRowCount(const [], 13), 13);
    expect(timetableRowCount([_courseEndingAt(4)], 13), 4);
    expect(timetableRowCount([_courseEndingAt(4), _courseEndingAt(9)], 13), 9);
    expect(timetableRowCount([_courseEndingAt(13)], 13), 13);
  });

  test('a course cannot draw rows past the semester table', () {
    expect(timetableRowCount([_courseEndingAt(9)], 8), 8);
  });
}