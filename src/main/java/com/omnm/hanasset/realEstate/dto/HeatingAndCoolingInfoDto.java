package com.omnm.hanasset.realEstate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeatingAndCoolingInfoDto {
    private String systemType;
    private String energyType;
}
