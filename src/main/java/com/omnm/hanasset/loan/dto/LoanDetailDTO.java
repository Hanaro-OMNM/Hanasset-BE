package com.omnm.hanasset.loan.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanDetailDTO {
    private Long loanId;
    private String name;
    private String type;
    private String outline;
    private String limit;
    private String rate;
    private String feature;
    private String targetGuest;
    private String targetHouse;
    private String period;
    private String paybackMethod;
    private String dsr;
}
