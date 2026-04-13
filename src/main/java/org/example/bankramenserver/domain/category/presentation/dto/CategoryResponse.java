package org.example.bankramenserver.domain.category.presentation.dto;

import org.example.bankramenserver.domain.category.domain.Category;

public record CategoryResponse(
        String code,
        String displayName
) {
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
                category.name(),
                category.getDisplayName()
        );
    }
}
