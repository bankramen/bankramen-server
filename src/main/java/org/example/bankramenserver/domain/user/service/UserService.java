package org.example.bankramenserver.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.bankramenserver.domain.auth.dto.KakaoUserResponse;
import org.example.bankramenserver.domain.user.repository.UserRepository;
import org.example.bankramenserver.domain.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User saveOrUpdate(KakaoUserResponse kakaoUser) {
        String kakaoId = String.valueOf(kakaoUser.id());

        return userRepository.findByKakaoId(kakaoId)
                .map(existing -> {
                    existing.updateProfile(
                            kakaoUser.kakaoAccount().profile().nickname(),
                            kakaoUser.kakaoAccount().profile().profileImageUrl()
                    );
                    return existing;
                })
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .kakaoId(kakaoId)
                                .email(kakaoUser.kakaoAccount().email())
                                .nickname(kakaoUser.kakaoAccount().profile().nickname())
                                .profileImageUrl(kakaoUser.kakaoAccount().profile().profileImageUrl())
                                .build()
                ));
    }
}
