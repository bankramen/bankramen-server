package org.example.bankramenserver.domain.recurring.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bankramenserver.domain.recurring.presentation.dto.request.CreateRecurringPaymentRequest;
import org.example.bankramenserver.domain.recurring.presentation.dto.response.ConfirmRecurringPaymentResponse;
import org.example.bankramenserver.domain.recurring.presentation.dto.response.CreateRecurringPaymentResponse;
import org.example.bankramenserver.domain.recurring.presentation.dto.response.DeleteRecurringPaymentResponse;
import org.example.bankramenserver.domain.recurring.presentation.dto.response.RecurringPaymentListResponse;
import org.example.bankramenserver.domain.recurring.service.ConfirmRecurringPaymentService;
import org.example.bankramenserver.domain.recurring.service.CreateRecurringPaymentService;
import org.example.bankramenserver.domain.recurring.service.DeleteRecurringPaymentService;
import org.example.bankramenserver.domain.recurring.service.GetRecurringPaymentsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recurring-payments")
@Tag(name = "정기결제 API", description = "정기결제 등록, 목록 조회, 삭제, 자동 감지 후보 확정 API")
public class RecurringPaymentController {

    private final CreateRecurringPaymentService createRecurringPaymentService;
    private final ConfirmRecurringPaymentService confirmRecurringPaymentService;
    private final GetRecurringPaymentsService getRecurringPaymentsService;
    private final DeleteRecurringPaymentService deleteRecurringPaymentService;

    @Operation(
            summary = "정기결제 목록 조회",
            description = "사용자의 활성 정기결제 목록과 이번 달 예정된 정기결제 총 결제 금액을 조회합니다."
    )
    @GetMapping
    public RecurringPaymentListResponse getRecurringPayments(
            @RequestHeader("userId") UUID userId
    ) {
        return getRecurringPaymentsService.execute(userId);
    }

    @Operation(
            summary = "정기결제 직접 등록",
            description = "사용자가 거래 기록을 기준으로 정기결제를 직접 등록합니다."
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateRecurringPaymentResponse create(
            @RequestHeader("userId") UUID userId,
            @Valid @RequestBody CreateRecurringPaymentRequest request
    ) {
        return createRecurringPaymentService.execute(userId, request);
    }

    @Operation(
            summary = "자동 감지 정기결제 후보 확정",
            description = "반복 결제로 자동 감지된 정기결제 후보를 사용자가 확인하여 확정 상태로 변경합니다."
    )
    @PatchMapping("/{recurringPaymentId}/confirm")
    public ConfirmRecurringPaymentResponse confirm(
            @RequestHeader("userId") UUID userId,
            @Parameter(description = "확정할 정기결제 ID")
            @PathVariable UUID recurringPaymentId
    ) {
        return confirmRecurringPaymentService.execute(userId, recurringPaymentId);
    }

    @Operation(
            summary = "정기결제 삭제",
            description = "정기결제를 비활성화합니다."
    )
    @DeleteMapping("/{recurringPaymentId}")
    public DeleteRecurringPaymentResponse delete(
            @RequestHeader("userId") UUID userId,
            @Parameter(description = "삭제할 정기결제 ID")
            @PathVariable UUID recurringPaymentId
    ) {
        return deleteRecurringPaymentService.execute(userId, recurringPaymentId);
    }
}