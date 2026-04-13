package org.example.bankramenserver.domain.transaction.presentation.dto;

import lombok.Builder;

import java.time.YearMonth;
import java.util.List;

@Builder
public record MonthlyIncomeTransactionListResponse(
        String yearMonth,
        List<TransactionHistoryResponse> incomes
) {

    public static MonthlyIncomeTransactionListResponse of(
            YearMonth yearMonth,
            List<TransactionHistoryResponse> incomes
    ) {
        return MonthlyIncomeTransactionListResponse.builder()
                .yearMonth(yearMonth.toString())
                .incomes(incomes)
                .build();
    }
}
