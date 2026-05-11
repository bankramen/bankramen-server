package org.example.bankramenserver.domain.push.domain.repository;

import org.example.bankramenserver.domain.push.domain.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {

    List<DeviceToken> findAllByMemberId(UUID memberId);

    void deleteByToken(String token);
}
