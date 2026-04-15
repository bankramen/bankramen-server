package org.example.bankramenserver.global.document;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.example.bankramenserver.domain.transaction.presentation.dto.MonthlyExpenseTransactionListResponse;
import org.example.bankramenserver.domain.transaction.presentation.dto.MonthlyIncomeTransactionListResponse;
import org.springframework.http.MediaType;

import java.util.UUID;

@Tag(name = "Transaction", description = "월별 수입/지출 거래 내역 조회 API")
public interface TransactionApiDocument {

    @Operation(
            summary = "월별 수입 내역 조회",
            description = "선택한 월의 수입 거래 내역을 제목, 거래일, 거래시간, 금액, 카테고리 정보와 함께 조회합니다.",
            tags = {"Transaction"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "월별 수입 내역 조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MonthlyIncomeTransactionListResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "요청 파라미터 검증 실패", content = @Content),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content)
    })
    MonthlyIncomeTransactionListResponse getMonthlyIncomeTransactions(
            @Parameter(description = "사용자 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @NotNull UUID userId,
            @Parameter(description = "조회 연도", required = true, example = "2026")
            @Min(2000) @Max(2999) int year,
            @Parameter(description = "조회 월", required = true, example = "8")
            @Min(1) @Max(12) int month
    );

    @Operation(
            summary = "월별 지출 내역 조회",
            description = "선택한 월의 지출 거래 내역을 제목, 거래일, 거래시간, 금액, 카테고리 정보와 함께 조회합니다.",
            tags = {"Transaction"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "월별 지출 내역 조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MonthlyExpenseTransactionListResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "요청 파라미터 검증 실패", content = @Content),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content)
    })
    MonthlyExpenseTransactionListResponse getMonthlyExpenseTransactions(
            @Parameter(description = "사용자 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @NotNull UUID userId,
            @Parameter(description = "조회 연도", required = true, example = "2026")
            @Min(2000) @Max(2999) int year,
            @Parameter(description = "조회 월", required = true, example = "8")
            @Min(1) @Max(12) int month
    );
}
