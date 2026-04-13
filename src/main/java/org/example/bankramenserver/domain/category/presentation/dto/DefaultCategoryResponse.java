package org.example.bankramenserver.domain.category.presentation.dto;

import org.example.bankramenserver.domain.category.domain.Category;

public record DefaultCategoryResponse(
        String code,
        String displayName
) {
    public static DefaultCategoryResponse from(Category category) {
        return new DefaultCategoryResponse(
                category.name(),
                category.getDisplayName()
        );
    }
}
