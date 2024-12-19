package com.omnm.hanasset.marker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Schema(description = "아파트 단지 마커 응답")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AptMarkersResponse {

    @Schema(description = "근처 아파트 단지 마커 정보")
    private List<AptMarkerDto> markerInfos;
}
