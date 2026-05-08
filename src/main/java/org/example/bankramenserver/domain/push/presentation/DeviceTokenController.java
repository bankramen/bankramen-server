package org.example.bankramenserver.domain.push.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bankramenserver.domain.push.presentation.dto.DeviceTokenRequest;
import org.example.bankramenserver.domain.push.service.DeviceTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/device-token")
@RequiredArgsConstructor
@Tag(name = "푸시 알림 API", description = "디바이스 토큰 관리 API")
public class DeviceTokenController {

    private final DeviceTokenService deviceTokenService;

    @Operation(
            summary = "디바이스 토큰 등록",
            description = "로그인한 사용자의 FCM 디바이스 토큰을 등록합니다."
    )
    @PostMapping
    public ResponseEntity<Void> register(
            @AuthenticationPrincipal UUID userId,
            @Valid @RequestBody DeviceTokenRequest request
    ) {
        deviceTokenService.register(userId, request.token());
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "디바이스 토큰 삭제",
            description = "로그아웃 또는 알림 해제 시 디바이스 토큰을 삭제합니다."
    )
    @DeleteMapping
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UUID userId,
            @Valid @RequestBody DeviceTokenRequest request
    ) {
        deviceTokenService.delete(userId, request.token());
        return ResponseEntity.ok().build();
    }
}