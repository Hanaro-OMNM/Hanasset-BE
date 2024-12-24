package com.omnm.hanasset.consultant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ConsultantSignInRequest {
    @NotBlank(message = "아이디를 입력해 주세요.")
    private String consultantLoginId;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
            message = "비밀번호는 최소 8자에서 20자 사이의 문자, 숫자, 특수 문자를 포함해야 합니다.")
    private String password;
}
