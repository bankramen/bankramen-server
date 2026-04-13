package org.example.bankramenserver.domain.report.presentation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.bankramenserver.domain.report.presentation.dto.MonthlyAmountSummaryResponse;
import org.example.bankramenserver.domain.report.presentation.dto.MonthlyCategoryExpenseListResponse;
import org.example.bankramenserver.domain.report.service.GetMonthlyAmountSummaryService;
import org.example.bankramenserver.domain.report.service.GetMonthlyCategoryExpenseListService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports/monthly")
public class MonthlyReportController {

    private final GetMonthlyAmountSummaryService getMonthlyAmountSummaryService;
    private final GetMonthlyCategoryExpenseListService getMonthlyCategoryExpenseListService;

    @GetMapping("/summary/{userId}")
    public MonthlyAmountSummaryResponse getMonthlyAmountSummary(
            @PathVariable @NotNull UUID userId,
            @RequestParam @Min(2000) @Max(2999) int year,
            @RequestParam @Min(1) @Max(12) int month
    ) {
        return getMonthlyAmountSummaryService.execute(userId, year, month);
    }

    @GetMapping("/categories/{userId}")
    public MonthlyCategoryExpenseListResponse getMonthlyCategoryExpenses(
            @PathVariable @NotNull UUID userId,
            @RequestParam @Min(2000) @Max(2999) int year,
            @RequestParam @Min(1) @Max(12) int month
    ) {
        return getMonthlyCategoryExpenseListService.execute(userId, year, month);
    }
}
