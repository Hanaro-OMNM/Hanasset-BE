package com.omnm.hanasset.realEstate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "매물 타입 정보 응답")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealEstateTypeResponse {

    @Schema(description = "매물의 타입 이름")
    private String name;

    @Schema(description = "매물의 타입 공급 면적")
    private Double supplyAreaSize;

    @Schema(description = "매물의 타입 전용 면적")
    private Double exclusiveAreaSize;

    @Schema(description = "매물의 타입 관리비")
    private Double managementFee;

    @Schema(description = "매물의 타입 방 개수")
    private Integer roomCount;

    @Schema(description = "매물의 타입 화장실 개수")
    private Integer bathroomCount;

    @Schema(description = "매물의 타입 평면도 이미지 주소")
    private String floorPlanImgUrl;

    @Schema(description = "매물의 타입 평면도 링크")
    private String floorPlanLink;
}