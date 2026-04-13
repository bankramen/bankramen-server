package org.example.bankramenserver.domain.report.domain.repository;

import org.example.bankramenserver.domain.category.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CategoryExpenseRow {

    private final Category category;
    private final Long totalExpense;
}
