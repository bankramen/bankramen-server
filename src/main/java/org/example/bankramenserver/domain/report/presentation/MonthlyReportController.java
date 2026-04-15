package org.example.bankramenserver.domain.report.presentation;

import lombok.RequiredArgsConstructor;
import org.example.bankramenserver.domain.report.presentation.dto.MonthlyAmountSummaryResponse;
import org.example.bankramenserver.domain.report.presentation.dto.MonthlyCategoryExpenseListResponse;
import org.example.bankramenserver.domain.report.service.GetMonthlyAmountSummaryService;
import org.example.bankramenserver.domain.report.service.GetMonthlyCategoryExpenseListService;
import org.example.bankramenserver.global.document.MonthlyReportApiDocument;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports/monthly")
public class MonthlyReportController implements MonthlyReportApiDocument {

    private final GetMonthlyAmountSummaryService getMonthlyAmountSummaryService;
    private final GetMonthlyCategoryExpenseListService getMonthlyCategoryExpenseListService;

    @Override
    @GetMapping("/summary/{userId}")
    public MonthlyAmountSummaryResponse getMonthlyAmountSummary(
            @PathVariable UUID userId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return getMonthlyAmountSummaryService.execute(userId, year, month);
    }

    @Override
    @GetMapping("/categories/{userId}")
    public MonthlyCategoryExpenseListResponse getMonthlyCategoryExpenses(
            @PathVariable UUID userId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return getMonthlyCategoryExpenseListService.execute(userId, year, month);
    }
}
