package com.omnm.hanasset.realEstate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "매물 시세 정보 응답")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealEstateMarketPriceResponse {

    @Schema(description = "외부 API 단지 code")
    private Integer complexNumber;

    @Schema(description = "외부 API 평형 code")
    private Integer pyeongTypeNumber;

    @Schema(description = "외부 API 거래 타입 code (B1: 전세, B2: 월세)")
    private String tradeType;
}
