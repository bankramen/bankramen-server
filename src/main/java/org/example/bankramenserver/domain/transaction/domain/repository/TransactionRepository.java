package org.example.bankramenserver.domain.transaction.domain.repository;

import org.example.bankramenserver.domain.transaction.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findAllByUserIdAndTypeAndTransactionDateBetweenOrderByTransactionDateDescCreatedAtDesc(
            UUID userId,
            Transaction.TransactionType type,
            LocalDate startDate,
            LocalDate endDate
    );
}
