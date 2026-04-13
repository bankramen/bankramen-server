package org.example.bankramenserver.domain.report.domain.repository;

import org.example.bankramenserver.domain.category.domain.Category;
import org.example.bankramenserver.domain.transaction.domain.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface MonthlyReportQueryRepository {

    long sumExpenseByUserAndPeriod(UUID userId, LocalDate startDate, LocalDate endDate);

    boolean existsExpenseByUserAndPeriod(UUID userId, LocalDate startDate, LocalDate endDate);

    long sumAmountByUserAndTypeAndPeriod(UUID userId, Transaction.TransactionType transactionType, LocalDate startDate, LocalDate endDate);

    boolean existsAmountByUserAndTypeAndPeriod(UUID userId, Transaction.TransactionType transactionType, LocalDate startDate, LocalDate endDate);

    List<CategoryExpenseRow> findCategoryExpenses(UUID userId, LocalDate startDate, LocalDate endDate);

}
