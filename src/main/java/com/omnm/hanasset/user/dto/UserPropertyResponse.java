package com.omnm.hanasset.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "손님 기본 정보 및 자산 정보")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPropertyResponse {
    @Schema(description = "유저 이름")
    private String name;
    @Schema(description = "나이")
    private Integer age;
    @Schema(description = "직업")
    private String jobType;
    @Schema(description = "연소득")
    private Integer income;
    @Schema(description = "자본금")
    private Integer capital;
    @Schema(description = "주택보유여부")
    private Boolean hasHouse;
    @Schema(description = "연이자상환액")
    private Integer annualInterest;
    @Schema(description = "연원금상환액")
    private Integer annualPrinciple;
    @Schema(description = "dsr")
    private Double dsr;
}
