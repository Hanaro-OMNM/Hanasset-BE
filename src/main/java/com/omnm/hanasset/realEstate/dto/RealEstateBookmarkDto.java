package com.omnm.hanasset.realEstate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(description = "북마크 여부가 포함된 매물 리스트")
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RealEstateBookmarkDto extends RealEstateDto {

    @Schema(description = "북마크 여부")
    private Boolean isBookmarked;
}
