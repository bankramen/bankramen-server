package org.example.bankramenserver.domain.category.presentation.dto;

import java.util.List;

public record DefaultCategoryListResponse(
        List<DefaultCategoryResponse> categories
) {
    public static DefaultCategoryListResponse from(List<DefaultCategoryResponse> categories) {
        return new DefaultCategoryListResponse(categories);
    }
}
