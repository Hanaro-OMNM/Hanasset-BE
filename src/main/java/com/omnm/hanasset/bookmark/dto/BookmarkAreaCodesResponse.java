package com.omnm.hanasset.bookmark.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "북마크 지역 리스트 응답")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkAreaCodesResponse {

    @Schema(description = "지역 리스트")
    private List<AreaCodeDto> areaCodes;

}
