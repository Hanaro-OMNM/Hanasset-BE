package com.omnm.hanasset.loan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "대출 상세보기 정보")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanDetailResponse {
    @Schema(description = "대출 상품 ID")
    private Long loanId;
    @Schema(description = "대출 상품 이름")
    private String name;
    @Schema(description = "대출 상품 종류, [전월세, 버팀목]")
    private String type;
    @Schema(description = "요약")
    private String outline;
    @Schema(description = "대출 한도")
    private String limit;
    @Schema(description = "대출 금리")
    private String rate;
    @Schema(description = "대출 특징")
    private String feature;
    @Schema(description = "대상 고객")
    private String targetGuest;
    @Schema(description = "대상 주택")
    private String targetHouse;
    @Schema(description = "대출 기간")
    private String period;
    @Schema(description = "상환 방식")
    private String paybackMethod;
    @Schema(description = "갱신 dsr")
    private String dsr;
}
