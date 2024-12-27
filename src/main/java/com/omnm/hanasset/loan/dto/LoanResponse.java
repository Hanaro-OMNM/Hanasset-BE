package com.omnm.hanasset.loan.dto;

import com.omnm.hanasset.user.dto.UserPropertyResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Schema(description = "대출 추천 정보")
@Data
@Builder
public class LoanResponse {
    @Schema(description = "유저 정보")
    UserPropertyResponse user;
    @Schema(description = "매물에 따른 하나은행 및 버팀목 대출 추천 정보")
    List<LoanRecommendInfoDTO> loanRecommendInfos;
}
