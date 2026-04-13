package org.example.bankramenserver.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.example.bankramenserver.domain.report.domain.repository.AmountSummaryRow;
import org.example.bankramenserver.domain.report.domain.repository.MonthlyReportRepository;
import org.example.bankramenserver.domain.report.presentation.dto.MonthlyAmountSummaryResponse;
import org.example.bankramenserver.domain.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetMonthlyAmountSummaryService {

    private final UserRepository userRepository;
    private final MonthlyReportRepository monthlyReportRepository;

    @Transactional(readOnly = true)
    public MonthlyAmountSummaryResponse execute(UUID userId, int year, int month) {
        validateUserExists(userId);

        YearMonth currentMonth = YearMonth.of(year, month);
        YearMonth previousMonth = currentMonth.minusMonths(1);
        AmountSummaryRow amountSummary = monthlyReportRepository.findAmountSummary(
                userId,
                currentMonth.atDay(1),
                currentMonth.atEndOfMonth(),
                previousMonth.atDay(1),
                previousMonth.atEndOfMonth()
        );

        return MonthlyAmountSummaryResponse.of(
                currentMonth,
                getAmountComparison(
                        amountSummary.currentExpense(),
                        amountSummary.previousExpense(),
                        amountSummary.previousExpenseCount()
                ),
                getAmountComparison(
                        amountSummary.currentIncome(),
                        amountSummary.previousIncome(),
                        amountSummary.previousIncomeCount()
                )
        );
    }

    private void validateUserExists(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다. userId=" + userId);
        }
    }

    private MonthlyAmountSummaryResponse.AmountComparison getAmountComparison(
            Long currentAmount,
            Long previousAmount,
            Long previousCount
    ) {
        long current = getAmount(currentAmount);
        long previous = getAmount(previousAmount);
        boolean hasPreviousMonthData = previousCount != null && previousCount > 0L;

        if (!hasPreviousMonthData) {
            return MonthlyAmountSummaryResponse.AmountComparison.withoutPreviousMonth(current);
        }

        return MonthlyAmountSummaryResponse.AmountComparison.of(
                current,
                previous,
                calculateRate(current - previous, previous)
        );
    }

    private long getAmount(Long amount) {
        return amount == null ? 0L : amount;
    }

    private BigDecimal calculateRate(long differenceAmount, long previousAmount) {
        if (previousAmount == 0L) {
            return null;
        }

        return BigDecimal.valueOf(differenceAmount)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(previousAmount), 1, RoundingMode.HALF_UP);
    }

}
