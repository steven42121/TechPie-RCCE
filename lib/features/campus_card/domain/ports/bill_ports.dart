import '../models/bill_models.dart';

abstract interface class BillSummaryRepository {
  Future<List<String>> availableMonths();
  Future<BillSummary> summary(String month);
}

abstract interface class TransactionHistoryPort {
  Future<TransactionPage> timeline({
    required String month,
    String? cursor,
    int pageSize = 20,
  });

  Future<TransactionRecord> detail(String id);
}

abstract interface class DateRangeTransactionHistoryPort {
  Future<TransactionPage> timelineRange({
    DateTime? begin,
    DateTime? end,
    String? cursor,
    int pageSize = 20,
  });
}

/// Records the app has just learned about, oldest first. The implementation
/// decides what the user is actually told — this is a signal, not a promise
/// that a message appears.
abstract interface class SpendingAlertPort {
  Future<void> alertNewSpending(List<TransactionRecord> records);
}
