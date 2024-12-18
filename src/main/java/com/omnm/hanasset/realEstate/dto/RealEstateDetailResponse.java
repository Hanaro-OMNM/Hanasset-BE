package com.omnm.hanasset.realEstate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealEstateDetailResponse {
    private Integer unitCount;
    private String entranceType;
    private FloorInfoDto floorInfo;
    private DirectionInfoDto directionInfo;
}
