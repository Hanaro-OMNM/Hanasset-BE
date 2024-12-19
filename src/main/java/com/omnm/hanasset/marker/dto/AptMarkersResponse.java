package com.omnm.hanasset.marker.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AptMarkersResponse {
    private List<AptMarkerDto> markerInfos;
}
