package com.omnm.hanasset.markers.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarkersAreaResponse {
    private CurrentMarkersDTO currentMarkers;
    private List<ShowMarkersDTO> markerInfos;
}