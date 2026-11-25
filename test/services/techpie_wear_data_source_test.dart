import 'package:flutter_secure_storage_ohos/flutter_secure_storage_ohos.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:techpie/features/campus_card/data/mock/in_memory_ports.dart';
import 'package:techpie/models/third_party_account.dart';
import 'package:techpie/models/user_session.dart';
import 'package:techpie/services/assignment_service.dart';
import 'package:techpie/services/auth_service.dart';
import 'package:techpie/services/campus_card_service.dart';
import 'package:techpie/services/debug_logger.dart';
import 'package:techpie/services/http_client.dart';
import 'package:techpie/services/schedule_service.dart';
import 'package:techpie/services/storage_service.dart';
import 'package:techpie/services/third_party_auth_service.dart';
import 'package:techpie/services/uni_auth_service.dart';
import 'package:techpie/services/wear/techpie_wear_data_source.dart';

import '../support/fake_ids_client.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  late ThirdPartyAuthService accounts;
  late TechPieWearDataSource source;

  setUp(() async {
    SharedPreferences.setMockInitialValues({});
    FlutterSecureStorage.setMockInitialValues({});
    final storage = StorageService(await SharedPreferences.getInstance());
    final http = LoggingHttpClient(DebugLogger());
    final auth = AuthService(storage, http, UniAuthService());
    await storage.saveSession(
      UserSession(
        userId: 'geekpie-user',
        userName: 'SSO name',
        studentId: '20230001',
        schoolName: 'School',
        createdAt: DateTime.utc(2026),
      ),
    );
    await auth.loadSession();
    accounts = ThirdPartyAuthService(storage, http, idsClient: FakeIdsClient());
    await accounts.initialize();
    final schedule = ScheduleService(storage, http, auth, accounts);
    final assignments = AssignmentService(storage, http, auth, accounts, schedule);
    final campusCard = CampusCardService.withStore(InMemorySecureCredentialStore());
    source = TechPieWearDataSource(
      auth: auth,
      accounts: accounts,
      assignments: assignments,
      campusCard: campusCard,
      timetable: schedule,
    );
    addTearDown(() {
      assignments.dispose();
      schedule.dispose();
      campusCard.dispose();
      accounts.dispose();
      auth.dispose();
      http.close();
    });
  });

  test('IDS identity reaches the watch without exposing legacy session UUIDs', () async {
    final account = FakeIdsClient.account('20240001', name: 'IDS name').copyWith(
      sid: '12345678-1234-1234-1234-123456789abc',
    );
    await accounts.replaceAll([account]);
    final before = await source.account();
    expect(before.name, 'IDS name');
    expect(before.studentId, '20240001');
    expect(before.scope, isNot(contains(account.sid!)));

    await accounts.replaceAll([account.copyWith(sid: 'rotated-session')]);
    expect((await source.account()).scope, before.scope);

    await accounts.replaceAll([FakeIdsClient.account('20240002', name: 'Other IDS name')]);
    final after = await source.account();
    expect(after.studentId, '20240002');
    expect(after.scope, isNot(before.scope));
  });

  test('unbinding IDS restores the complete SSO identity', () async {
    await accounts.replaceAll([FakeIdsClient.account('20240001', name: 'IDS name')]);
    await accounts.unbind(ThirdPartyPlatform.ids);
    final account = await source.account();
    expect(account.name, 'SSO name');
    expect(account.studentId, '20230001');
  });
}
