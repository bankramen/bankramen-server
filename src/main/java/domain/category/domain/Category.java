package domain.category.domain;

import domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "name"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private CategoryType type;

    @Column(name = "icon")
    private String icon;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault = false;

    @Builder
    public Category(User user, String name, CategoryType type, String icon, boolean isDefault) {
        this.user = user;
        this.name = name;
        this.type = type;
        this.icon = icon;
        this.isDefault = isDefault;
    }

    public void update(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    public enum CategoryType {
        INCOME, EXPENSE
    }
}