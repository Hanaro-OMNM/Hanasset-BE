package com.omnm.hanasset.marker.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Schema(description = "근처 지역 마커")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShowMarkersDTO {

    @Schema(description = "지역 이름")
    private String name;

    @Schema(description = "법정동 코드")
    private Long cortarNoCode;

    @Schema(description = "중심 위도")
    private double centerLat;

    @Schema(description = "중심 경도")
    private double centerLng;
}
