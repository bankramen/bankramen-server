package org.example.bankramenserver.domain.push.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bankramenserver.domain.user.domain.User;
import org.example.bankramenserver.global.common.BaseEntity;

@Entity
@Getter
@Table(
        name = "device_token",
        indexes = {
                @Index(name = "idx_device_token_user_id", columnList = "user_id")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeviceToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true, length = 512)
    private String token;

    @Builder
    public DeviceToken(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public void updateUser(User user) {
        this.user = user;
    }

    public void updateToken(String token) {
        this.token = token;
    }
}