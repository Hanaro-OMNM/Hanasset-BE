package com.omnm.hanasset.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "손님 자산 정보 불러오기 응답")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class PropertyResponse {
    @Schema(description = "손님 직업", example = "대기업")
    private String jobType;

    @Schema(description = "손님 연소득", example = "5000")
    private Integer income;

    @Schema(description = "손님 자본금", example = "5000")
    private Integer capital;

    @Schema(description = "손님 보유 주택 여부", example = "false")
    private Boolean hasHouse;

    @Schema(description = "손님 보유 대출 연이자 상환액", example = "100")
    private Integer annualInterest;

    @Schema(description = "손님 보유 대출 연원금 상환액", example = "100")
    private Integer annualPrincipal;

    @Schema(description = "손님 비정상거처여부", example = "false")
    private Boolean isAbnormalHouse;

    @Schema(description = "손님 전세피해여부", example = "false")
    private Boolean isHousingFraudVictim;

    @Schema(description = "손님 Stress DSR", example = "12.0")
    private Double stressDsr;
}
