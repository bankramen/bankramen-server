package org.example.bankramenserver.domain.recurring.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.bankramenserver.domain.category.domain.Category;
import org.example.bankramenserver.domain.transaction.domain.Transaction;
import org.example.bankramenserver.domain.user.domain.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "recurring_payments")
public class RecurringPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Cycle cycle;

    @Column(nullable = false)
    private int billingDay;

    @Column(nullable = false)
    private LocalDateTime nextBillingDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RegistrationType registrationType;

    @Column(nullable = false)
    @Builder.Default
    private boolean confirmed = false;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @OneToMany(mappedBy = "recurringPayment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RecurringPaymentTransaction> transactions = new ArrayList<>();

    public void confirm() {
        this.confirmed = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void updateAfterPaymentDetected(LocalDateTime nextBillingDate) {
        this.nextBillingDate = nextBillingDate;
    }

    public LocalDateTime calculateNextBillingDate() {
        if (cycle == Cycle.MONTHLY) {
            return nextBillingDate.plusMonths(1);
        }

        return nextBillingDate.plusYears(1);
    }

    public void addTransaction(
            Transaction transaction,
            RecurringPaymentTransaction.MatchType matchType,
            LocalDateTime matchedAt
    ) {
        boolean alreadyAdded = transactions.stream()
                .anyMatch(item -> item.getTransaction().getId().equals(transaction.getId()));

        if (alreadyAdded) {
            return;
        }

        RecurringPaymentTransaction recurringPaymentTransaction = RecurringPaymentTransaction.builder()
                .recurringPayment(this)
                .transaction(transaction)
                .matchType(matchType)
                .matchedAt(matchedAt)
                .build();

        this.transactions.add(recurringPaymentTransaction);
    }

    public enum Cycle {
        MONTHLY,
        YEARLY
    }

    public enum RegistrationType {
        MANUAL,
        AUTO_DETECTED
    }
}