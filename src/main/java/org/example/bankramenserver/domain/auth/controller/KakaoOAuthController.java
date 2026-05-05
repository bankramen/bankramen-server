package org.example.bankramenserver.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bankramenserver.domain.auth.dto.TokenRequest;
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
@Tag(name = "인증 API", description = "카카오 OAuth 로그인 및 인증 관리 관련 API")
public class KakaoOAuthController {

    private final KakaoOAuthService kakaoOAuthService;
    private final StateService stateService;

    @Operation(summary = "카카오 로그인 URL 발급", description = "카카오 로그인 페이지로 이동하기 위한 인증 URL을 생성하여 반환합니다.")
    @GetMapping("/login")
    public ResponseEntity<?> login() {
        String state = stateService.generateState();
        String loginUrl = kakaoOAuthService.getLoginUrl(state);
        return ResponseEntity.ok(Map.of("loginUrl", loginUrl));
    }

    @Operation(summary = "카카오 로그인 콜백", description = "카카오 인증 서버로부터 리다이렉트된 콜백을 받아 로그인을 처리합니다.")
    @GetMapping("/callback")
    public ResponseEntity<?> callback(
            @Parameter(description = "카카오 인가 코드") @RequestParam String code,
            @Parameter(description = "상태 값(보안용)") @RequestParam String state
    ) {
        if (!stateService.validateState(state)) {
            throw InvalidStateException.EXCEPTION;
        }

        Map<String, String> tokens = kakaoOAuthService.kakaoLogin(code);
        return ResponseEntity.ok(tokens);
    }

    @Operation(summary = "토큰 재발급", description = "유효한 리프레시 토큰을 통해 새로운 액세스 토큰을 재발급합니다.")
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@Valid @RequestBody TokenRequest request) {
        String accessToken = kakaoOAuthService.reissue(request.refreshToken());
        return ResponseEntity.ok(Map.of("accessToken", accessToken));
    }

    @Operation(summary = "로그아웃", description = "리프레시 토큰을 사용하여 로그아웃 처리를 수행합니다.")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody TokenRequest request) {
        kakaoOAuthService.logout(request.refreshToken());
        return ResponseEntity.ok().build();
    }
}