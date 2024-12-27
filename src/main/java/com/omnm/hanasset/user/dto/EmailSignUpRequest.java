package com.omnm.hanasset.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Schema(description = "이메일 회원가입 요청")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EmailSignUpRequest {
    @Schema(description = "회원가입 이름", example = "변우석")
    @NotBlank(message = "이름을 입력해 주세요.")
    private String name;

    @Schema(description = "로그인 이메일", example = "test@example.com")
    @NotBlank(message = "이메일을 입력해 주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "올바른 이메일 형식을 입력해 주세요")
    private String email;

    @Schema(description = "로그인 비밀번호", example = "1234567Q!*")
    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
            message = "비밀번호는 최소 8자에서 20자 사이의 문자, 숫자, 특수 문자를 포함해야 합니다.")
    private String password;

    @Schema(description = "비밀번호 재확인", example = "1234567Q!*")
    @NotBlank(message = "비밀번호를 한 번 더 입력해 주세요.")
    private String confirmPassword;

    @AssertTrue(message = "비밀번호가 일치하지 않습니다.")
    private boolean isPasswordConfirmed() {
        return password.equals(confirmPassword);
    }
}
