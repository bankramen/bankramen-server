package org.example.bankramenserver.domain.transaction.presentation.dto;

import lombok.Builder;
import org.example.bankramenserver.domain.category.domain.Category;
import org.example.bankramenserver.domain.transaction.domain.repository.TransactionHistoryRow;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record TransactionHistoryResponse(
        String title,
        LocalDate transactionDate,
        LocalTime transactionTime,
        long amount,
        Category category,
        String categoryName
) {

    public static TransactionHistoryResponse from(TransactionHistoryRow transactionHistoryRow) {
        Category category = transactionHistoryRow.category();

        return TransactionHistoryResponse.builder()
                .title(transactionHistoryRow.title())
                .transactionDate(transactionHistoryRow.transactionDate())
                .transactionTime(resolveTransactionTime(transactionHistoryRow))
                .amount(transactionHistoryRow.amount() == null ? 0L : transactionHistoryRow.amount())
                .category(category)
                .categoryName(category.getDisplayName())
                .build();
    }

    private static LocalTime resolveTransactionTime(TransactionHistoryRow transactionHistoryRow) {
        if (transactionHistoryRow.createdAt() == null) {
            return null;
        }

        return transactionHistoryRow.createdAt().toLocalTime();
    }
}
