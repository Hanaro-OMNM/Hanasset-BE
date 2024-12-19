package com.omnm.hanasset.realEstate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "방향 정보")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DirectionInfoDto {

    @Schema(description = "기준")
    private String standard;

    @Schema(description = "방향")
    private String facing;
}
