package com.omnm.hanasset.consultant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Schema(description = "상담사 로그인 요청")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ConsultantSignInRequest {
    @Schema(description = "상담사 로그인 아이디")
    @NotBlank(message = "아이디를 입력해 주세요.")
    private String consultantLoginId;

    @Schema(description = "상담사 로그인 비밀번호")
    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
            message = "비밀번호는 최소 8자에서 20자 사이의 문자, 숫자, 특수 문자를 포함해야 합니다.")
    private String password;
}
