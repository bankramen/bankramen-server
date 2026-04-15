package org.example.bankramenserver.domain.transaction.service;

import lombok.RequiredArgsConstructor;
import org.example.bankramenserver.domain.transaction.domain.Transaction;
import org.example.bankramenserver.domain.transaction.domain.repository.TransactionRepository;
import org.example.bankramenserver.domain.transaction.presentation.dto.MonthlyExpenseTransactionListResponse;
import org.example.bankramenserver.domain.transaction.presentation.dto.TransactionHistoryResponse;
import org.example.bankramenserver.domain.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetMonthlyExpenseTransactionListService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public MonthlyExpenseTransactionListResponse execute(UUID userId, int year, int month) {
        validateUserExists(userId);

        YearMonth yearMonth = YearMonth.of(year, month);
        List<TransactionHistoryResponse> expenses = transactionRepository
                .findTransactionHistories(
                        userId,
                        Transaction.TransactionType.EXPENSE,
                        yearMonth.atDay(1),
                        yearMonth.atEndOfMonth()
                )
                .stream()
                .map(TransactionHistoryResponse::from)
                .toList();

        return MonthlyExpenseTransactionListResponse.of(yearMonth, expenses);
    }

    private void validateUserExists(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다. userId=" + userId);
        }
    }
}
