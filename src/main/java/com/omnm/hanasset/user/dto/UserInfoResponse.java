package com.omnm.hanasset.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Schema(description = "손님 기본 정보 응답")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UserInfoResponse {
    private String name;
    private String email;
    private LocalDate birthDate;
}
