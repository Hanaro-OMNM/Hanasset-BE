package com.omnm.hanasset.loan.dto;

import com.omnm.hanasset.realEstate.dto.RealEstateInfoResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Schema(description = "추천 대출 상품 정보")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanRecommendInfoDTO {
    @Schema(description = "매물 데이터 정보")
    RealEstateInfoResponse realEstateInfo;
    @Schema(description = "하나은행 대출 상품 추천 목록")
    List<LoanInfoDTO> hanaLoans;
    @Schema(description = "버팀목 대출 상품 추천 목록")
    List<LoanInfoDTO> beotimmokLoans;
}
