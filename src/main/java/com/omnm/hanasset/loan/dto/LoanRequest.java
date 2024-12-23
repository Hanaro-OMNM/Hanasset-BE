package com.omnm.hanasset.loan.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LoanRequest {
    private List<Long> realEstateIds;
}
