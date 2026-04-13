package org.example.bankramenserver.domain.report.presentation.dto;

import org.example.bankramenserver.domain.category.domain.Category;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public record MonthlyCategoryExpenseListResponse(
        String yearMonth,
        long totalExpense,
        List<CategoryExpense> categories
) {

    public static MonthlyCategoryExpenseListResponse of(
            YearMonth yearMonth,
            long totalExpense,
            List<CategoryExpense> categories
    ) {
        return new MonthlyCategoryExpenseListResponse(yearMonth.toString(), totalExpense, categories);
    }

    public record CategoryExpense(
            Category category,
            String categoryName,
            long expenseAmount,
            BigDecimal expenseRatio,
            boolean spentMoreThanPreviousMonth
    ) {

        public static CategoryExpense of(
                Category category,
                String categoryName,
                long expenseAmount,
                BigDecimal expenseRatio,
                boolean spentMoreThanPreviousMonth
        ) {
            return new CategoryExpense(
                    category,
                    categoryName,
                    expenseAmount,
                    expenseRatio,
                    spentMoreThanPreviousMonth
            );
        }
    }
}
