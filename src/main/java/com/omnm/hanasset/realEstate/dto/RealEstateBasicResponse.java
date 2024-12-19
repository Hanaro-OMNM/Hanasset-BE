package com.omnm.hanasset.realEstate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "매물 기본 정보 응답")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealEstateBasicResponse {

    @Schema(description = "매물의 단지 주소")
    private String address;

    @Schema(description = "매물의 단지 총 세대 수")
    private Integer unitCount;

    @Schema(description = "매물의 단지 총 주차 수")
    private Integer parkingCount;

    @Schema(description = "매물의 단지 설립 날짜")
    private String establishedDate;

    @Schema(description = "매물의 단지 총 동 수")
    private Integer dongCount;

    @Schema(description = "매물의 단지 난방 및 냉방 정보")
    private HeatingAndCoolingInfoDto heatingAndCoolingInfo;

    @Schema(description = "매물의 단지 건축비율 정보")
    private BuildingRatioInfoDto buildingRatioInfo;

    @Schema(description = "매물의 단지 건설사")
    private String constructionCompany;
}
