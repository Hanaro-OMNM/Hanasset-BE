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
    @Schema(description = "손님 이름", example = "변우석")
    private String name;

    @Schema(description = "손님 이메일", example = "test@example.com")
    private String email;

    @Schema(description = "손님 생년월일", example = "1991-10-31")
    private LocalDate birthDate;
}
