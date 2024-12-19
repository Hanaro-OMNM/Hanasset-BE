package com.omnm.hanasset.markers.dto;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShowMarkersDTO {
    private String name;
    private Long cortarNoCode;
    private double centerLat;
    private double centerLng;
}
