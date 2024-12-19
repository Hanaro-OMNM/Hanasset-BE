package com.omnm.hanasset.marker.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AreaMarkersResponse {
    private CurrentMarkersDTO currentMarkers;
    private List<ShowMarkersDTO> markerInfos;
}