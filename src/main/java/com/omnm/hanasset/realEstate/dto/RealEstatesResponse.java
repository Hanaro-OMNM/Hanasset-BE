package com.omnm.hanasset.realEstate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "매물 리스트 응답")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealEstatesResponse {

    @Schema(description = "매물 개수")
    private Integer count;

    @Schema(description = "매물 리스트")
    private List<RealEstateDto> realEstates;
}
