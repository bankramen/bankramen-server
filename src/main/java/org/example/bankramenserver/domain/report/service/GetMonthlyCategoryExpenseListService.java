package org.example.bankramenserver.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.example.bankramenserver.domain.category.domain.Category;
import org.example.bankramenserver.domain.report.domain.repository.CategoryExpenseRow;
import org.example.bankramenserver.domain.report.domain.repository.MonthlyReportRepository;
import org.example.bankramenserver.domain.report.presentation.dto.MonthlyCategoryExpenseListResponse;
import org.example.bankramenserver.domain.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetMonthlyCategoryExpenseListService {

    private final UserRepository userRepository;
    private final MonthlyReportRepository monthlyReportRepository;

    @Transactional(readOnly = true)
    public MonthlyCategoryExpenseListResponse execute(UUID userId, int year, int month) {
        validateUserExists(userId);

        YearMonth currentMonth = YearMonth.of(year, month);
        YearMonth previousMonth = currentMonth.minusMonths(1);
        long totalExpense = monthlyReportRepository.sumExpenseByUserAndPeriod(
                userId,
                currentMonth.atDay(1),
                currentMonth.atEndOfMonth()
        );
        Map<Category, CategoryExpenseRow> previousRows = monthlyReportRepository.findCategoryExpenses(
                        userId,
                        previousMonth.atDay(1),
                        previousMonth.atEndOfMonth()
                )
                .stream()
                .collect(Collectors.toMap(CategoryExpenseRow::getCategory, Function.identity(), (left, right) -> left));

        List<MonthlyCategoryExpenseListResponse.CategoryExpense> categories =
                monthlyReportRepository.findCategoryExpenses(userId, currentMonth.atDay(1), currentMonth.atEndOfMonth())
                        .stream()
                        .map(row -> toCategoryExpense(row, previousRows.get(row.getCategory()), totalExpense))
                        .toList();

        return MonthlyCategoryExpenseListResponse.of(
                currentMonth,
                totalExpense,
                categories
        );
    }

    private void validateUserExists(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다. userId=" + userId);
        }
    }

    private MonthlyCategoryExpenseListResponse.CategoryExpense toCategoryExpense(
            CategoryExpenseRow row,
            CategoryExpenseRow previousRow,
            long totalExpense
    ) {
        long expenseAmount = row.getTotalExpense() == null ? 0L : row.getTotalExpense();
        long previousExpenseAmount = previousRow == null || previousRow.getTotalExpense() == null ? 0L : previousRow.getTotalExpense();

        return MonthlyCategoryExpenseListResponse.CategoryExpense.of(
                row.getCategory(),
                row.getCategory().getDisplayName(),
                expenseAmount,
                calculateRatio(expenseAmount, totalExpense),
                expenseAmount > previousExpenseAmount
        );
    }

    private BigDecimal calculateRatio(long amount, long totalExpense) {
        if (totalExpense <= 0) {
            return BigDecimal.ZERO.setScale(1, RoundingMode.HALF_UP);
        }

        return BigDecimal.valueOf(amount)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(totalExpense), 1, RoundingMode.HALF_UP);
    }

}
