package com.omnm.hanasset.realEstate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "대출 상담에 필요한 매물 정보")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealEstatesInfoResponse {
    @Schema(description = "매물 ID")
    private Long realEstateId;

    @Schema(description = "매물 이름")
    private String name;

    @Schema(description = "매물 거래 종류")
    private String rentType;

    @Schema(description = "매물의 단지 주소")
    private String address;

    @Schema(description = "매물 상세 주소")
    private String addressDetail;

    @Schema(description = "매물 전세금 또는 보증금")
    private Integer deposit;

    @Schema(description = "매물의 타입 전용 면적")
    private Double exclusiveAreaSize;

}
