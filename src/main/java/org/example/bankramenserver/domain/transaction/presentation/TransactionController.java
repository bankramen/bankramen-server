package org.example.bankramenserver.domain.transaction.presentation;

import lombok.RequiredArgsConstructor;
import org.example.bankramenserver.domain.transaction.presentation.dto.MonthlyExpenseTransactionListResponse;
import org.example.bankramenserver.domain.transaction.presentation.dto.MonthlyIncomeTransactionListResponse;
import org.example.bankramenserver.domain.transaction.service.GetMonthlyExpenseTransactionListService;
import org.example.bankramenserver.domain.transaction.service.GetMonthlyIncomeTransactionListService;
import org.example.bankramenserver.global.document.TransactionApiDocument;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController implements TransactionApiDocument {

    private final GetMonthlyIncomeTransactionListService getMonthlyIncomeTransactionListService;
    private final GetMonthlyExpenseTransactionListService getMonthlyExpenseTransactionListService;

    @Override
    @GetMapping("/incomes/{userId}")
    public MonthlyIncomeTransactionListResponse getMonthlyIncomeTransactions(
            @PathVariable UUID userId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return getMonthlyIncomeTransactionListService.execute(userId, year, month);
    }

    @Override
    @GetMapping("/expenses/{userId}")
    public MonthlyExpenseTransactionListResponse getMonthlyExpenseTransactions(
            @PathVariable UUID userId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return getMonthlyExpenseTransactionListService.execute(userId, year, month);
    }
}
