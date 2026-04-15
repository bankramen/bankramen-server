package org.example.bankramenserver.domain.transaction.domain.repository;

import org.example.bankramenserver.domain.transaction.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID>, TransactionRepositoryCustom {
}
