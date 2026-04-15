package org.example.bankramenserver.domain.transaction.domain.repository;

import lombok.Builder;
import org.example.bankramenserver.domain.category.domain.Category;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record TransactionHistoryRow(
        String title,
        LocalDate transactionDate,
        LocalDateTime createdAt,
        Long amount,
        Category category
) { }
