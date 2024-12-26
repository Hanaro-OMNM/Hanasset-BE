package com.omnm.hanasset.bookmark.dto;

import com.omnm.hanasset.realEstate.dto.RealEstateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "북마크 리스트 응답")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkResponse {

    @Schema(description = "지역 리스트")
    private List<AreaCodeDto> areaCodes;

    @Schema(description = "매물 리스트")
    private List<RealEstateDto> realEstates;

}
