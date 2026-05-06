package org.example.bankramenserver.domain.transaction.service;

import lombok.RequiredArgsConstructor;
import org.example.bankramenserver.domain.category.domain.Category;
import org.example.bankramenserver.domain.transaction.domain.Transaction;
import org.example.bankramenserver.domain.transaction.domain.repository.TransactionRepository;
import org.example.bankramenserver.domain.transaction.presentation.dto.CreatePaymentNotificationTransactionRequest;
import org.example.bankramenserver.domain.user.domain.User;
import org.example.bankramenserver.domain.user.domain.repository.UserRepository;
import org.example.bankramenserver.domain.user.facade.UserFacade;
import org.example.bankramenserver.global.ai.CategoryRecommendationClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CreatePaymentNotificationTransactionService {

    private final UserFacade userFacade;
    private final CategoryRecommendationClient categoryRecommendationClient;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final Clock clock;

    @Transactional
    public void execute(CreatePaymentNotificationTransactionRequest request) {
        Category recommendedCategory = categoryRecommendationClient.recommend(request.title())
                .orElse(Category.UNCATEGORIZED);
        User currentUser = userRepository.getReferenceById(userFacade.getCurrentUserId());

        Transaction transaction = Transaction.builder()
                .user(currentUser)
                .category(recommendedCategory)
                .type(Transaction.TransactionType.EXPENSE)
                .amount(request.amount())
                .description(request.title())
                .source(Transaction.TransactionSource.NOTIFICATION)
                .transactionDate(LocalDate.now(clock))
                .build();

        transactionRepository.save(transaction);
    }
}
