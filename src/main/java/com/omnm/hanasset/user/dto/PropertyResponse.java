package com.omnm.hanasset.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
