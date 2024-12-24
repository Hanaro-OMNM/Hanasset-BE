package com.omnm.hanasset.loan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "대출 추천 시 대출 카드에 보이는 정보")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanInfoDTO {
    @Schema(description = "대출 상품 ID")
    private Long loanId;
    @Schema(description = "대출 상품 이름")
    private String name;
    @Schema(description = "대출 금리")
    private Double rate;
    @Schema(description = "대출 한도")
    private Integer limitAmount;
    @Schema(description = "갱신 dsr")
    private Double dsr;
}
