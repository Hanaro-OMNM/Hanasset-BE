package com.omnm.hanasset.search.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "매물 검색 결과 응답")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class SearchResponse {
    @Schema(description = "단지 테이블 Id", example = "1")
    private Long housingComplexId;

    @Schema(description = "단지 이름", example = "한강 극동")
    private String complexName;

    @Schema(description = "단지 주소(구 - 동)", example = "송파구 풍납동")
    private String addressName;

    @Schema(description = "위도", example = "37.5267982")
    private Double lat;

    @Schema(description = "경도", example = "127.1283743")
    private Double lng;
}
