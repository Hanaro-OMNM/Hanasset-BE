package com.omnm.hanasset.realEstate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealEstateTypeResponse {
    private String name;
    private Double supplyAreaSize;
    private Double exclusiveAreaSize;
    private Double managementFee;
    private Integer roomCount;
    private Integer bathroomCount;
    private String floorPlanImgUrl;
    private String floorPlanLink;
}