package com.omnm.hanasset.loan.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class LoanRequest {
    private Long userId;
    private List<Long> realEstateIds = new ArrayList<>();
}
