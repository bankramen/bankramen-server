package org.example.bankramenserver.domain.push.service;

import lombok.RequiredArgsConstructor;
import org.example.bankramenserver.domain.push.domain.DeviceToken;
import org.example.bankramenserver.domain.push.domain.repository.DeviceTokenRepository;
import org.example.bankramenserver.domain.user.domain.User;
import org.example.bankramenserver.domain.user.domain.repository.UserRepository;
import org.example.bankramenserver.domain.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceTokenService {

    private final DeviceTokenRepository deviceTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public void register(UUID userId, String token) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        deviceTokenRepository.findByToken(token)
                .ifPresentOrElse(
                        deviceToken -> deviceToken.updateUser(user),
                        () -> deviceTokenRepository.save(
                                DeviceToken.builder()
                                        .user(user)
                                        .token(token)
                                        .build()
                        )
                );
    }

    @Transactional
    public void delete(UUID userId, String token) {
        deviceTokenRepository.deleteByUserIdAndToken(userId, token);
    }
}