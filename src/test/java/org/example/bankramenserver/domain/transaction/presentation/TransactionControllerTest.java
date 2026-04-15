package org.example.bankramenserver.domain.transaction.presentation;

import org.example.bankramenserver.domain.category.domain.Category;
import org.example.bankramenserver.domain.transaction.presentation.dto.MonthlyExpenseTransactionListResponse;
import org.example.bankramenserver.domain.transaction.presentation.dto.MonthlyIncomeTransactionListResponse;
import org.example.bankramenserver.domain.transaction.presentation.dto.TransactionHistoryResponse;
import org.example.bankramenserver.domain.transaction.service.GetMonthlyExpenseTransactionListService;
import org.example.bankramenserver.domain.transaction.service.GetMonthlyIncomeTransactionListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    private static final UUID USER_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    @Mock
    private GetMonthlyIncomeTransactionListService getMonthlyIncomeTransactionListService;

    @Mock
    private GetMonthlyExpenseTransactionListService getMonthlyExpenseTransactionListService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TransactionController(
                        getMonthlyIncomeTransactionListService,
                        getMonthlyExpenseTransactionListService
                ))
                .build();
    }

    @Test
    void getMonthlyIncomeTransactionsReturnsIncomeHistories() throws Exception {
        MonthlyIncomeTransactionListResponse response = MonthlyIncomeTransactionListResponse.of(
                YearMonth.of(2026, 8),
                List.of(new TransactionHistoryResponse(
                        "월급",
                        LocalDate.of(2026, 8, 25),
                        LocalTime.of(9, 30),
                        3500000L,
                        Category.SALARY,
                        "급여"
                ))
        );

        when(getMonthlyIncomeTransactionListService.execute(USER_ID, 2026, 8)).thenReturn(response);

        mockMvc.perform(get("/transactions/incomes/{userId}", USER_ID)
                        .param("year", "2026")
                        .param("month", "8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.yearMonth").value("2026-08"))
                .andExpect(jsonPath("$.incomes[0].title").value("월급"))
                .andExpect(jsonPath("$.incomes[0].transactionDate").value("2026-08-25"))
                .andExpect(jsonPath("$.incomes[0].transactionTime").value("09:30:00"))
                .andExpect(jsonPath("$.incomes[0].amount").value(3500000))
                .andExpect(jsonPath("$.incomes[0].category").value("SALARY"))
                .andExpect(jsonPath("$.incomes[0].categoryName").value("급여"));

        verify(getMonthlyIncomeTransactionListService).execute(USER_ID, 2026, 8);
    }

    @Test
    void getMonthlyExpenseTransactionsReturnsExpenseHistories() throws Exception {
        MonthlyExpenseTransactionListResponse response = MonthlyExpenseTransactionListResponse.of(
                YearMonth.of(2026, 8),
                List.of(new TransactionHistoryResponse(
                        "스타벅스 강남점",
                        LocalDate.of(2026, 8, 12),
                        LocalTime.of(14, 30),
                        1400L,
                        Category.FOOD,
                        "식비"
                ))
        );

        when(getMonthlyExpenseTransactionListService.execute(USER_ID, 2026, 8)).thenReturn(response);

        mockMvc.perform(get("/transactions/expenses/{userId}", USER_ID)
                        .param("year", "2026")
                        .param("month", "8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.yearMonth").value("2026-08"))
                .andExpect(jsonPath("$.expenses[0].title").value("스타벅스 강남점"))
                .andExpect(jsonPath("$.expenses[0].transactionDate").value("2026-08-12"))
                .andExpect(jsonPath("$.expenses[0].transactionTime").value("14:30:00"))
                .andExpect(jsonPath("$.expenses[0].amount").value(1400))
                .andExpect(jsonPath("$.expenses[0].category").value("FOOD"))
                .andExpect(jsonPath("$.expenses[0].categoryName").value("식비"));

        verify(getMonthlyExpenseTransactionListService).execute(USER_ID, 2026, 8);
    }
}
