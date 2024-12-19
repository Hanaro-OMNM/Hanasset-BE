package com.omnm.hanasset.markers.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrentMarkersDTO {
    private Long cortarNo;
    private String cityName;
    private String sigunguName;
    private String emdName;
}