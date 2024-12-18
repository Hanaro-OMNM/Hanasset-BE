package com.omnm.hanasset.realEstate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealEstateDto {
    private Long realEstateId;
    private String imgUrl;
    private String type;
    private String rentType;
    private String name;
    private String addressDetail;
    private Integer floor;
    private Integer deposit;
    private Integer price;
    private String description;
}
