package com.omnm.hanasset.realEstate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "난방 및 냉방 정보")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeatingAndCoolingInfoDto {

    @Schema(description = "난방 방식")
    private String systemType;

    @Schema(description = "에너지 종류")
    private String energyType;
}
