package org.example.bankramenserver.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.example.bankramenserver.domain.report.domain.repository.MonthlyReportRepository;
import org.example.bankramenserver.domain.report.presentation.dto.MonthlyAmountSummaryResponse;
import org.example.bankramenserver.domain.transaction.domain.Transaction;
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

        return MonthlyAmountSummaryResponse.of(
                currentMonth,
                getAmountComparison(userId, Transaction.TransactionType.EXPENSE, currentMonth, previousMonth),
                getAmountComparison(userId, Transaction.TransactionType.INCOME, currentMonth, previousMonth)
        );
    }

    private void validateUserExists(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다. userId=" + userId);
        }
    }

    private MonthlyAmountSummaryResponse.AmountComparison getAmountComparison(
            UUID userId,
            Transaction.TransactionType transactionType,
            YearMonth currentMonth,
            YearMonth previousMonth
    ) {
        long currentAmount = monthlyReportRepository.sumAmountByUserAndTypeAndPeriod(
                userId,
                transactionType,
                currentMonth.atDay(1),
                currentMonth.atEndOfMonth()
        );

        boolean hasPreviousMonthData = monthlyReportRepository.existsAmountByUserAndTypeAndPeriod(
                userId,
                transactionType,
                previousMonth.atDay(1),
                previousMonth.atEndOfMonth()
        );

        if (!hasPreviousMonthData) {
            return MonthlyAmountSummaryResponse.AmountComparison.withoutPreviousMonth(currentAmount);
        }

        long previousAmount = monthlyReportRepository.sumAmountByUserAndTypeAndPeriod(
                userId,
                transactionType,
                previousMonth.atDay(1),
                previousMonth.atEndOfMonth()
        );
        long differenceAmount = currentAmount - previousAmount;

        return MonthlyAmountSummaryResponse.AmountComparison.of(
                currentAmount,
                previousAmount,
                calculateRate(differenceAmount, previousAmount)
        );
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
