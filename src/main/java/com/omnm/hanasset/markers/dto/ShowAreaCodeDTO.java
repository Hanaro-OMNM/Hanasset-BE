package com.omnm.hanasset.markers.dto;

import lombok.*;
import org.locationtech.jts.geom.Point;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShowAreaCodeDTO {
    private String name;
    private Long code;
    private Point coordinate;
}
