package org.example.bankramenserver.domain.transaction.event;

import org.example.bankramenserver.domain.category.domain.Category;

import java.util.UUID;

public record PaymentTransactionRecordedEvent(
        UUID userId,
        UUID transactionId,
        String title,
        Long amount,
        Category category
) {
}
