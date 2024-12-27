package com.omnm.hanasset.bookmark.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "북마크 지역 상태 응답")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkAreaCodesStatusResponse {

    @Schema(description = "최대 북마크 개수 (3개) 초과 여부")
    private boolean isFull;

    @Schema(description = "삭제 예정 지역 이름")
    private String emdName;
}
