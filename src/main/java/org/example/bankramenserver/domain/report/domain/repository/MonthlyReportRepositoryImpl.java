package org.example.bankramenserver.domain.report.domain.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.bankramenserver.domain.category.domain.Category;
import org.example.bankramenserver.domain.transaction.domain.QTransaction;
import org.example.bankramenserver.domain.transaction.domain.Transaction;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MonthlyReportRepositoryImpl implements MonthlyReportQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private static final QTransaction transaction = QTransaction.transaction;

    @Override
    public long sumExpenseByUserAndPeriod(UUID userId, LocalDate startDate, LocalDate endDate) {
        return sumAmountByUserAndTypeAndPeriod(userId, Transaction.TransactionType.EXPENSE, startDate, endDate);
    }

    @Override
    public boolean existsExpenseByUserAndPeriod(UUID userId, LocalDate startDate, LocalDate endDate) {
        return existsAmountByUserAndTypeAndPeriod(userId, Transaction.TransactionType.EXPENSE, startDate, endDate);
    }

    @Override
    public long sumAmountByUserAndTypeAndPeriod(UUID userId, Transaction.TransactionType transactionType, LocalDate startDate, LocalDate endDate) {
        Long result = jpaQueryFactory
                .select(transaction.amount.sum().coalesce(0L))
                .from(transaction)
                .where(
                        transaction.user.id.eq(userId),
                        transaction.type.eq(transactionType),
                        transaction.transactionDate.between(startDate, endDate)
                )
                .fetchOne();

        return result == null ? 0L : result;
    }

    @Override
    public boolean existsAmountByUserAndTypeAndPeriod(UUID userId, Transaction.TransactionType transactionType, LocalDate startDate, LocalDate endDate) {
        Integer fetched = jpaQueryFactory
                .selectOne()
                .from(transaction)
                .where(
                        transaction.user.id.eq(userId),
                        transaction.type.eq(transactionType),
                        transaction.transactionDate.between(startDate, endDate)
                )
                .fetchFirst();

        return fetched != null;
    }

    @Override
    public List<CategoryExpenseRow> findCategoryExpenses(UUID userId, LocalDate startDate, LocalDate endDate) {
        return jpaQueryFactory
                .select(Projections.constructor(
                        CategoryExpenseRow.class,
                        transaction.category,
                        transaction.amount.sum().coalesce(0L)
                ))
                .from(transaction)
                .where(
                        transaction.user.id.eq(userId),
                        transaction.type.eq(Transaction.TransactionType.EXPENSE),
                        transaction.transactionDate.between(startDate, endDate)
                )
                .groupBy(transaction.category)
                .orderBy(
                        transaction.amount.sum().coalesce(0L).desc()
                )
                .fetch();
    }
}
