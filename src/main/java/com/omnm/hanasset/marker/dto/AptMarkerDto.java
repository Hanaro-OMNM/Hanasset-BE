package com.omnm.hanasset.marker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "아파트 단지 마커")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AptMarkerDto {

    @Schema(description = "아파트 단지 ID")
    private Long housingComplexId;

    @Schema(description = "아파트 단지 이름")
    private String name;

    @Schema(description = "아파트 단지 중심 위도")
    private Double centerLat;

    @Schema(description = "아파트 단지 코드 경도")
    private Double centerLng;
}
