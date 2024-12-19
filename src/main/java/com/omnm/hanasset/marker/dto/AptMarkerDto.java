package com.omnm.hanasset.marker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AptMarkerDto {
    private Long housingComplexId;
    private String name;
    private Double centerLat;
    private Double centerLng;
}
