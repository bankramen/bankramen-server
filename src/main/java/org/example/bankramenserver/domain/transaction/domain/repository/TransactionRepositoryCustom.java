package org.example.bankramenserver.domain.transaction.domain.repository;

import org.example.bankramenserver.domain.transaction.domain.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionRepositoryCustom {

    List<TransactionHistoryRow> findTransactionHistories(
            UUID userId,
            Transaction.TransactionType transactionType,
            LocalDate startDate,
            LocalDate endDate
    );
}
