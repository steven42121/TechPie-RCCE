import 'dart:async';

import 'package:flutter/services.dart';

import '../features/campus_card/domain/models/bill_models.dart';
import '../features/campus_card/domain/ports/bill_ports.dart';
import '../utils/platform.dart';
import 'background_refresher.dart';
import 'storage_service.dart';

/// Announces card spending the app has just learned about.
///
/// The history refresh is the only source: the campus card has no push channel,
/// so a payment is reported as soon as the app next reads the ledger, not the
/// second the terminal prints the receipt.
final class SpendingAlertService implements SpendingAlertPort {
  SpendingAlertService(
    this._storage, {
    MethodChannel? channel,
  }) : _channel = channel ?? const MethodChannel('techpie/notification');

  /// More than this at one refresh is a backlog, not a shopping trip; the rest
  /// is folded into the last message instead of becoming a wall of notifications.
  static const maxPerRefresh = 3;

  final StorageService _storage;
  final MethodChannel _channel;

  bool get supported => isAndroid();

  bool get enabled => supported && _storage.spendingAlertsEnabled;

  Future<void> setEnabled(bool value) async {
    await _storage.setSpendingAlertsEnabled(value);
    // On or off, the schedule follows: the background wake-up exists for
    // whatever the user has asked to be told about.
    unawaited(syncBackgroundRefresh(_storage));
    if (value && supported) {
      try {
        await _channel.invokeMethod<bool>('ensurePermission');
      } on PlatformException {
        // The toggle stays useful even when the prompt cannot be raised.
      }
    }
  }

  @override
  Future<void> alertNewSpending(List<TransactionRecord> records) async {
    if (!enabled) return;
    final spending = records
        .where((record) => record.kind == TransactionKind.consumption)
        .toList();
    if (spending.isEmpty) return;

    final alreadyAlerted = _storage.alertedTransactionIds.toSet();
    final fresh = spending
        .where((record) => !alreadyAlerted.contains(record.id))
        .toList();
    if (fresh.isEmpty) return;

    final shown = fresh.take(maxPerRefresh).toList();
    final folded = fresh.length - shown.length;
    for (var i = 0; i < shown.length; i++) {
      final isLast = i == shown.length - 1;
      await _post(
        shown[i],
        // One sound per burst: three receipts are one shopping trip.
        silent: i > 0,
        tail: isLast && folded > 0 ? '另有 $folded 笔' : null,
      );
    }
    // Everything read counts as announced: re-offering the folded ones on the
    // next refresh would show a week-old payment as if it were new.
    await _storage.setAlertedTransactionIds(
      <String>[...alreadyAlerted, ...fresh.map((record) => record.id)].toList(),
    );
  }

  Future<void> _post(
    TransactionRecord record, {
    required bool silent,
    String? tail,
  }) async {
    try {
      await _channel.invokeMethod<void>('post', {
        'key': record.id,
        'title': '消费提醒',
        'body': _body(record, tail: tail),
        'silent': silent,
      });
    } on PlatformException {
      // A notification the system refuses is not worth retrying: the ledger
      // screen has the same numbers.
    } on MissingPluginException {
      // Desktop and web have no notification host.
    }
  }

  String _body(TransactionRecord record, {String? tail}) {
    final amount = record.amount.toYuanFixed();
    final where = record.merchantName ?? record.location ?? record.title;
    final parts = <String>[
      amount.startsWith('-') ? amount : '-$amount',
      if (where.isNotEmpty) where,
      if (record.balance != null) '余额 ${record.balance!.toYuanFixed()}',
      if (tail != null) tail,
    ];
    return parts.join(' · ');
  }
}
