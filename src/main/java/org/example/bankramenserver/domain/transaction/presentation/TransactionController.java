package org.example.bankramenserver.domain.transaction.presentation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.bankramenserver.domain.transaction.presentation.dto.MonthlyExpenseTransactionListResponse;
import org.example.bankramenserver.domain.transaction.presentation.dto.MonthlyIncomeTransactionListResponse;
import org.example.bankramenserver.domain.transaction.service.GetMonthlyExpenseTransactionListService;
import org.example.bankramenserver.domain.transaction.service.GetMonthlyIncomeTransactionListService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final GetMonthlyIncomeTransactionListService getMonthlyIncomeTransactionListService;
    private final GetMonthlyExpenseTransactionListService getMonthlyExpenseTransactionListService;

    @GetMapping("/incomes/{userId}")
    public MonthlyIncomeTransactionListResponse getMonthlyIncomeTransactions(
            @PathVariable @NotNull UUID userId,
            @RequestParam @Min(2000) @Max(2999) int year,
            @RequestParam @Min(1) @Max(12) int month
    ) {
        return getMonthlyIncomeTransactionListService.execute(userId, year, month);
    }

    @GetMapping("/expenses/{userId}")
    public MonthlyExpenseTransactionListResponse getMonthlyExpenseTransactions(
            @PathVariable @NotNull UUID userId,
            @RequestParam @Min(2000) @Max(2999) int year,
            @RequestParam @Min(1) @Max(12) int month
    ) {
        return getMonthlyExpenseTransactionListService.execute(userId, year, month);
    }
}
