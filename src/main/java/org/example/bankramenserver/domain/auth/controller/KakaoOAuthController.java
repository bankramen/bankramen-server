package org.example.bankramenserver.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import org.example.bankramenserver.domain.auth.service.KakaoOAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth/kakao")
@RequiredArgsConstructor
public class KakaoOAuthController {

    private final KakaoOAuthService kakaoOAuthService;

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam String code) {
        String token = kakaoOAuthService.kakaoLogin(code);
        return ResponseEntity.ok(Map.of("token", token));
    }
}