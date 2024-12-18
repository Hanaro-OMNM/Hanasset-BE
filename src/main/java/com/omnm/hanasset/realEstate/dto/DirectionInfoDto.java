package com.omnm.hanasset.realEstate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DirectionInfoDto {
    private String standard;
    private String facing;
}
