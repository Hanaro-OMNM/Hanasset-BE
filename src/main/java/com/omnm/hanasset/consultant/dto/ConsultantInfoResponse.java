package com.omnm.hanasset.consultant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "상담사 정보 조회 응답")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ConsultantInfoResponse {
    @Schema(description = "상담사 DB 테이블 Id ex) 1")
    private Long consultantId;

    @Schema(description = "상담사의 이름 ex) 하나은행 상담사 김미강")
    private String name;
}
