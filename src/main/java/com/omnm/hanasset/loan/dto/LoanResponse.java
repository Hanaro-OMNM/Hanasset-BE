package com.omnm.hanasset.loan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Schema(description = "대출 추천 정보")
@Data
@Builder
public class LoanResponse {
    /**
     * TODO GuestDTO 추가
     * GuestDTO guest;
     */
    List<LoanRecommendInfoDTO> loanRecommendInfos;
}
