package com.omnm.hanasset.realEstate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "층 정보")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FloorInfoDto {

    @Schema(description = "전체 층")
    private Integer total;

    @Schema(description = "해당 층")
    private Integer target;
}
