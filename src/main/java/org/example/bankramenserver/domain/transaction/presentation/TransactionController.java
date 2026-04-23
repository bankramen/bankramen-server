package org.example.bankramenserver.domain.transaction.presentation;

import lombok.RequiredArgsConstructor;
import org.example.bankramenserver.domain.transaction.presentation.dto.MonthlyExpenseTransactionListResponse;
import org.example.bankramenserver.domain.transaction.presentation.dto.MonthlyIncomeTransactionListResponse;
import org.example.bankramenserver.domain.transaction.service.GetMonthlyExpenseTransactionListService;
import org.example.bankramenserver.domain.transaction.service.GetMonthlyIncomeTransactionListService;
import org.example.bankramenserver.global.document.TransactionApiDocument;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController implements TransactionApiDocument {

    private final GetMonthlyIncomeTransactionListService getMonthlyIncomeTransactionListService;
    private final GetMonthlyExpenseTransactionListService getMonthlyExpenseTransactionListService;

    @Override
    @GetMapping("/incomes")
    public MonthlyIncomeTransactionListResponse getMonthlyIncomeTransactions(
            @RequestParam int year,
            @RequestParam int month
    ) {
        return getMonthlyIncomeTransactionListService.execute(year, month);
    }

    @Override
    @GetMapping("/expenses")
    public MonthlyExpenseTransactionListResponse getMonthlyExpenseTransactions(
            @RequestParam int year,
            @RequestParam int month
    ) {
        return getMonthlyExpenseTransactionListService.execute(year, month);
    }
}
