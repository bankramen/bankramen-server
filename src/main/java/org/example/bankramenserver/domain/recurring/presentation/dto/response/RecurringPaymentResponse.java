package org.example.bankramenserver.domain.recurring.presentation.dto.response;

import org.example.bankramenserver.domain.recurring.domain.RecurringPayment;

import java.time.LocalDate;
import java.util.UUID;

public record RecurringPaymentResponse(
        UUID recurringPaymentId,
        String name,
        Long amount,
        String category,
        String categoryDisplayName,
        RecurringPayment.Cycle cycle,
        int billingDay,
        LocalDate nextBillingDate,
        RecurringPayment.RegistrationType registrationType,
        boolean confirmed,
        int transactionCount,
        LocalDate lastPaidDate
) {
    public static RecurringPaymentResponse from(RecurringPayment recurringPayment) {
        LocalDate lastPaidDate = recurringPayment.getTransactions().stream()
                .map(item -> item.getTransaction().getTransactionDate())
                .max(LocalDate::compareTo)
                .orElse(null);

        return new RecurringPaymentResponse(
                recurringPayment.getId(),
                recurringPayment.getName(),
                recurringPayment.getAmount(),
                recurringPayment.getCategory().name(),
                recurringPayment.getCategory().getDisplayName(),
                recurringPayment.getCycle(),
                recurringPayment.getBillingDay(),
                recurringPayment.getNextBillingDate().toLocalDate(),
                recurringPayment.getRegistrationType(),
                recurringPayment.isConfirmed(),
                recurringPayment.getTransactions().size(),
                lastPaidDate
        );
    }
}