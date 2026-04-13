package org.example.bankramenserver.domain.transaction.presentation.dto;

import lombok.Builder;
import org.example.bankramenserver.domain.category.domain.Category;
import org.example.bankramenserver.domain.transaction.domain.Transaction;

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

    public static TransactionHistoryResponse from(Transaction transaction) {
        Category category = transaction.getCategory();

        return TransactionHistoryResponse.builder()
                .title(transaction.getDescription())
                .transactionDate(transaction.getTransactionDate())
                .transactionTime(resolveTransactionTime(transaction))
                .amount(transaction.getAmount())
                .category(category)
                .categoryName(category.getDisplayName())
                .build();
    }

    private static LocalTime resolveTransactionTime(Transaction transaction) {
        if (transaction.getCreatedAt() == null) {
            return null;
        }

        return transaction.getCreatedAt().toLocalTime();
    }
}
