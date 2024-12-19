package com.omnm.hanasset.marker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "현재 지역 마커")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrentMarkersDTO {

    @Schema(description = "법정동 코드")
    private Long cortarNo;

    @Schema(description = "시/도 이름")
    private String cityName;

    @Schema(description = "시/군/구 이름")
    private String sigunguName;

    @Schema(description = "읍/면/동 이름")
    private String emdName;
}