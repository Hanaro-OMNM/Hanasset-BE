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
    private String jobType;
    private Integer income;
    private Integer capital;
    private Boolean hasHouse;
    private Integer annualInterest;
    private Integer annualPrincipal;
    private Boolean isAbnormalHouse;
    private Boolean isHousingFraudVictim;
    private Double stressDsr;
}
