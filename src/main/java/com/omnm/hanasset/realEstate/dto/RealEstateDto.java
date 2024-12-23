package com.omnm.hanasset.realEstate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "매물 정보")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealEstateDto {

    @Schema(description = "매물 ID")
    private Long realEstateId;

    @Schema(description = "매물 이미지 주소")
    private String imgUrl;

    @Schema(description = "매물 종류")
    private String type;

    @Schema(description = "매물 거래 종류")
    private String rentType;

    @Schema(description = "매물 이름")
    private String name;

    @Schema(description = "매물 상세 주소")
    private String addressDetail;

    @Schema(description = "매물 층")
    private Integer floor;

    @Schema(description = "매물 전세금 또는 보증금")
    private Integer deposit;

    @Schema(description = "매물 월세")
    private Integer price;

    @Schema(description = "매물 설명")
    private String description;
}
