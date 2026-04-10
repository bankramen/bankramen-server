package org.example.bankramenserver.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bankramenserver.domain.auth.exception.InvalidStateException;
import org.example.bankramenserver.domain.auth.service.KakaoOAuthService;
import org.example.bankramenserver.domain.auth.service.StateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth/kakao")
@RequiredArgsConstructor
public class KakaoOAuthController {

    private final KakaoOAuthService kakaoOAuthService;
    private final StateService stateService;

    @GetMapping("/login")
    public ResponseEntity<?> login() {
        String state = stateService.generateState();
        String loginUrl = kakaoOAuthService.getLoginUrl(state);
        return ResponseEntity.ok(Map.of("loginUrl", loginUrl));
    }

    @GetMapping("/callback")
    public ResponseEntity<?> callback(
            @RequestParam String code,
            @RequestParam String state
    ) {
        if (!stateService.validateState(state)) {
            throw InvalidStateException.EXCEPTION;
        }

        Map<String, String> tokens = kakaoOAuthService.kakaoLogin(code);
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody Map<String, String> request) {
        String accessToken = kakaoOAuthService.reissue(request.get("refreshToken"));
        return ResponseEntity.ok(Map.of("accessToken", accessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> request) {
        kakaoOAuthService.logout(request.get("refreshToken"));
        return ResponseEntity.ok().build();
    }
}