package com.omnm.hanasset.realEstate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "건축비율 정보")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuildingRatioInfoDto {

    @Schema(description = "용적률")
    private Double floorAreaRatio;

    @Schema(description = "건폐율")
    private Double buildingCoverageRatio;
}
