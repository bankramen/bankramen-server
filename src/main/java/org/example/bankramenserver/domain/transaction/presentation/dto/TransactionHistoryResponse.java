package org.example.bankramenserver.domain.transaction.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.example.bankramenserver.domain.category.domain.Category;
import org.example.bankramenserver.domain.transaction.domain.repository.TransactionHistoryRow;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Schema(description = "거래 내역 응답")
public record TransactionHistoryResponse(
        @Schema(description = "거래 제목 또는 설명", example = "스타벅스 강남점")
        String title,
        @Schema(description = "거래 날짜", example = "2026-08-15")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate transactionDate,
        @Schema(description = "거래 시간. 별도 거래 시간이 없으면 생성 시간을 기준으로 응답합니다.", example = "14:30:00", nullable = true)
        @JsonFormat(pattern = "HH:mm:ss")
        LocalTime transactionTime,
        @Schema(description = "거래 금액", example = "1400")
        long amount,
        @Schema(description = "카테고리 enum 코드", example = "FOOD")
        Category category,
        @Schema(description = "사용자에게 표시할 카테고리명", example = "식비")
        String categoryName
) {

    public static TransactionHistoryResponse from(TransactionHistoryRow transactionHistoryRow) {
        Category category = transactionHistoryRow.category();

        return TransactionHistoryResponse.builder()
                .title(transactionHistoryRow.title())
                .transactionDate(transactionHistoryRow.transactionDate())
                .transactionTime(resolveTransactionTime(transactionHistoryRow))
                .amount(transactionHistoryRow.amount() == null ? 0L : transactionHistoryRow.amount())
                .category(category)
                .categoryName(category.getDisplayName())
                .build();
    }

    private static LocalTime resolveTransactionTime(TransactionHistoryRow transactionHistoryRow) {
        if (transactionHistoryRow.createdAt() == null) {
            return null;
        }

        return transactionHistoryRow.createdAt().toLocalTime();
    }
}
