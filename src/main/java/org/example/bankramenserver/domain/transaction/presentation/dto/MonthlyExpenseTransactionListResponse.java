package org.example.bankramenserver.domain.transaction.presentation.dto;

import lombok.Builder;

import java.time.YearMonth;
import java.util.List;

@Builder
public record MonthlyExpenseTransactionListResponse(
        String yearMonth,
        List<TransactionHistoryResponse> expenses
) {

    public static MonthlyExpenseTransactionListResponse of(
            YearMonth yearMonth,
            List<TransactionHistoryResponse> expenses
    ) {
        return MonthlyExpenseTransactionListResponse.builder()
                .yearMonth(yearMonth.toString())
                .expenses(expenses)
                .build();
    }
}
