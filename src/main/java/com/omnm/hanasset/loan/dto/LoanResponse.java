package com.omnm.hanasset.loan.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class LoanResponse {
    /**
     * TODO GuestDTO 추가
     * GuestDTO guest;
     */
    List<LoanRecommendDTO> loanRecommendDTOS = new ArrayList<>();
}
