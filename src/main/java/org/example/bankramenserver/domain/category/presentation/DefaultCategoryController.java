package org.example.bankramenserver.domain.category.presentation;

import lombok.RequiredArgsConstructor;
import org.example.bankramenserver.domain.category.domain.Category;
import org.example.bankramenserver.domain.category.presentation.dto.DefaultCategoryListResponse;
import org.example.bankramenserver.domain.category.presentation.dto.DefaultCategoryResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class DefaultCategoryController {

    @GetMapping
    public DefaultCategoryListResponse getDefaultCategories() {
        List<DefaultCategoryResponse> categories = Arrays.stream(Category.values())
                .map(DefaultCategoryResponse::from)
                .toList();

        return DefaultCategoryListResponse.from(categories);
    }
}
