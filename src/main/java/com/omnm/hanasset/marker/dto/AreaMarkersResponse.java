package com.omnm.hanasset.marker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Schema(description = "지역 마커 응답")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AreaMarkersResponse {

    @Schema(description = "현재 지역 마커 정보")
    private CurrentMarkersDTO currentMarkers;

    @Schema(description = "근처 지역 마커 정보")
    private List<ShowMarkersDTO> markerInfos;
}