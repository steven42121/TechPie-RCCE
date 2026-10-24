import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:shared_preferences/shared_preferences.dart';

import 'features/campus_card/domain/ports/bill_ports.dart';
import 'services/assignment_service.dart';
import 'services/auth_service.dart';
import 'services/campus_card_service.dart';
import 'services/debug_logger.dart';
import 'services/http_client.dart';
import 'services/island_service.dart';
import 'services/schedule_service.dart';
import 'services/spending_alert_service.dart';
import 'services/storage_service.dart';
import 'services/third_party_auth_service.dart';
import 'services/uni_auth_service.dart';

/// What one background wake-up does: refresh the island from the cached
/// schedule, and report card spending the ledger now shows.
///
/// Runs headless — no widget tree, no user, and no engine kept alive once
/// [runBackgroundRefresh] answers `done`. Whatever fails here is a missed tick:
/// the next run is already scheduled.
Future<void> runBackgroundRefresh() async {
  WidgetsFlutterBinding.ensureInitialized();
  try {
    await _refresh();
  } catch (_) {
    // Nothing to report to anyone; the host has its own timeout.
  }
  try {
    await const MethodChannel('techpie/background').invokeMethod<bool>('done');
  } on PlatformException {
    // The host went away first.
  } on MissingPluginException {
    // No host at all: there was never anyone to answer.
  }
}

Future<void> _refresh() async {
  final prefs = await SharedPreferences.getInstance();
  final storage = StorageService(prefs);
  final logger = DebugLogger();
  final http = LoggingHttpClient(logger);
  final uniAuth = UniAuthService(logger: logger);
  final auth = AuthService(storage, http, uniAuth, logger: logger);
  final tpAuth = ThirdPartyAuthService(storage, http);
  final schedule = ScheduleService(storage, http, auth, tpAuth);
  final assignments = AssignmentService(storage, http, auth, tpAuth, schedule);
  final island = IslandService(storage, schedule, assignments);
  final spending = SpendingAlertService(storage);
  final campusCard = CampusCardService(debugLogger: logger, storage: storage);

  await schedule.loadCachedData();
  assignments.loadCached();
  await island.refreshNow(force: true);
  await _reportSpending(campusCard, storage, spending);
}

Future<void> _reportSpending(
  CampusCardService card,
  StorageService storage,
  SpendingAlertService alerts,
) async {
  if (!alerts.enabled) return;
  try {
    card.prepare();
    final port = card.runtime.transactions;
    if (port is! DateRangeTransactionHistoryPort) return;
    // Unrelated interfaces, so no promotion: the cast is the narrowing.
    final page = await (port as DateRangeTransactionHistoryPort).timelineRange();
    // A first-ever read has no baseline: recording what is old news beats
    // announcing a month of receipts, the way the in-app feed does it too.
    if (storage.alertedTransactionIds.isEmpty) {
      await storage.setAlertedTransactionIds(
        page.items.map((item) => item.id).toList(),
      );
      return;
    }
    await alerts.alertNewSpending(page.items);
  } catch (_) {
    // No session, no network, no news: the in-app path catches up later.
  }
}
