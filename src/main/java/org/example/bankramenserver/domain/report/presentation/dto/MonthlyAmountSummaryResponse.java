package org.example.bankramenserver.domain.report.presentation.dto;

import java.math.BigDecimal;
import java.time.YearMonth;

public record MonthlyAmountSummaryResponse(
        String yearMonth,
        AmountComparison expense,
        AmountComparison income
) {

    public static MonthlyAmountSummaryResponse of(
            YearMonth yearMonth,
            AmountComparison expense,
            AmountComparison income
    ) {
        return new MonthlyAmountSummaryResponse(yearMonth.toString(), expense, income);
    }

    public record AmountComparison(
            long currentAmount,
            Long previousAmount,
            boolean hasPreviousMonthData,
            BigDecimal differenceRate
    ) {

        public static AmountComparison withoutPreviousMonth(long currentAmount) {
            return new AmountComparison(
                    currentAmount,
                    null,
                    false,
                    null
            );
        }

        public static AmountComparison of(
                long currentAmount,
                long previousAmount,
                BigDecimal differenceRate
        ) {
            return new AmountComparison(
                    currentAmount,
                    previousAmount,
                    true,
                    differenceRate
            );
        }
    }
}
