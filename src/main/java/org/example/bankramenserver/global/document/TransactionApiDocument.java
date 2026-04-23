package org.example.bankramenserver.global.document;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.example.bankramenserver.domain.transaction.presentation.dto.MonthlyExpenseTransactionListResponse;
import org.example.bankramenserver.domain.transaction.presentation.dto.MonthlyIncomeTransactionListResponse;
import org.springframework.http.MediaType;

@Tag(name = "Transaction", description = "월별 수입/지출 거래 내역 조회 API")
public interface TransactionApiDocument {

    @Operation(
            summary = "월별 수입 내역 조회",
            description = "Authorization 헤더의 액세스 토큰으로 현재 사용자를 식별하고, 선택한 월의 수입 거래 내역을 제목, 거래일, 거래시간, 금액, 카테고리 정보와 함께 조회합니다.",
            tags = {"Transaction"}
    )
    @SecurityRequirement(name = "bearerAuth")
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
            @Parameter(description = "조회 연도", required = true, example = "2026")
            @Min(2000) @Max(2999) int year,
            @Parameter(description = "조회 월", required = true, example = "8")
            @Min(1) @Max(12) int month
    );

    @Operation(
            summary = "월별 지출 내역 조회",
            description = "Authorization 헤더의 액세스 토큰으로 현재 사용자를 식별하고, 선택한 월의 지출 거래 내역을 제목, 거래일, 거래시간, 금액, 카테고리 정보와 함께 조회합니다.",
            tags = {"Transaction"}
    )
    @SecurityRequirement(name = "bearerAuth")
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
            @Parameter(description = "조회 연도", required = true, example = "2026")
            @Min(2000) @Max(2999) int year,
            @Parameter(description = "조회 월", required = true, example = "8")
            @Min(1) @Max(12) int month
    );
}
