package com.omnm.hanasset.realEstate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealEstateBasicResponse {
    private String address;
    private Integer unitCount;
    private Integer parkingCount;
    private String establishedDate;
    private Integer dongCount;
    private HeatingAndCoolingInfoDto heatingAndCoolingInfo;
    private BuildingRatioInfoDto buildingRatioInfo;
    private String constructionCompany;
}
