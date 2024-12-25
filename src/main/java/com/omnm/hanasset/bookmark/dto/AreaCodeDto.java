package com.omnm.hanasset.bookmark.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "지역 정보")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AreaCodeDto {

    @Schema(description = "지역 ID")
    private Long areaCodeId;

    @Schema(description = "지역 이름")
    private String emdName;

    @Schema(description = "지역의 중심 위도")
    private Double centerLat;

    @Schema(description = "지역의 중심 경도")
    private Double centerLng;
}
