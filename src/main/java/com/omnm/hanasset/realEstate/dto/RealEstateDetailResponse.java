package com.omnm.hanasset.realEstate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "매물 상세 정보 응답")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealEstateDetailResponse {

    @Schema(description = "매물의 타입 총 세대 수")
    private Integer unitCount;

    @Schema(description = "매물의 타입 입구 타입")
    private String entranceType;

    @Schema(description = "매물의 층 정보")
    private FloorInfoDto floorInfo;

    @Schema(description = "매물의 방향 정보")
    private DirectionInfoDto directionInfo;
}
