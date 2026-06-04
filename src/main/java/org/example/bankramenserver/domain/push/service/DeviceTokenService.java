package org.example.bankramenserver.domain.push.service;

import lombok.RequiredArgsConstructor;
import org.example.bankramenserver.domain.push.domain.DeviceToken;
import org.example.bankramenserver.domain.push.domain.repository.DeviceTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DeviceTokenService {

    private final DeviceTokenRepository deviceTokenRepository;

    public void save(UUID memberId, String token) {

        deviceTokenRepository.findByToken(token)
                .ifPresentOrElse(
                        existing -> existing.updateMember(memberId),
                        () -> deviceTokenRepository.save(
                                DeviceToken.builder()
                                        .memberId(memberId)
                                        .token(token)
                                        .build()
                        )
                );
    }

    public void delete(UUID memberId) {
        deviceTokenRepository.deleteByMemberId(memberId);
    }
}