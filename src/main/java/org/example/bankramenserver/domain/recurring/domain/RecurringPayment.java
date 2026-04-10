package org.example.bankramenserver.domain.recurring.domain;

import org.example.bankramenserver.domain.user.entity.User;
import org.example.bankramenserver.domain.category.domain.Category;
import org.example.bankramenserver.domain.notification.domain.NotificationSource;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "recurring_payments",
        indexes = {
                @Index(name = "idx_recurring_user_confirmed", columnList = "user_id, is_confirmed"),
                @Index(name = "idx_recurring_next_billing", columnList = "next_billing_date")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class RecurringPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_source_id")
    private NotificationSource notificationSource;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "cycle", nullable = false)
    private Cycle cycle;

    @Column(name = "billing_day", nullable = false)
    private int billingDay;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "is_confirmed", nullable = false)
    private boolean isConfirmed = false;

    @Column(name = "next_billing_date")
    private LocalDateTime nextBillingDate;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public RecurringPayment(User user, Category category, NotificationSource notificationSource,
                            String name, Long amount, Cycle cycle, int billingDay, LocalDateTime nextBillingDate) {
        this.user = user;
        this.category = category;
        this.notificationSource = notificationSource;
        this.name = name;
        this.amount = amount;
        this.cycle = cycle;
        this.billingDay = billingDay;
        this.isActive = true;
        this.isConfirmed = false;
        this.nextBillingDate = nextBillingDate;
    }

    public void confirm() {
        this.isConfirmed = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void updateNextBillingDate(LocalDateTime nextBillingDate) {
        this.nextBillingDate = nextBillingDate;
    }

    public enum Cycle {
        MONTHLY, YEARLY
    }
}