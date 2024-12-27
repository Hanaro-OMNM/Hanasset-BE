package com.omnm.hanasset.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Schema(description = "손님 기본 정보 수정 요청")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserInfoRequest {
    @Schema(description = "변경하려는 이름", example = "유연석")
    @NotBlank(message = "이름을 입력해 주세요.")
    private String name;

    @Schema(description = "기존 비밀번호", example = "1234567@!*")
    @NotBlank(message = "기존 비밀번호를 입력해 주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
            message = "비밀번호는 최소 8자에서 20자 사이의 문자, 숫자, 특수 문자를 포함해야 합니다.")
    private String currPassword;

    @Schema(description = "변경하려는 비밀번호", example = "7890123@!*")
    @NotBlank(message = "새 비밀번호를 입력해 주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
            message = "비밀번호는 최소 8자에서 20자 사이의 문자, 숫자, 특수 문자를 포함해야 합니다.")
    private String newPassword;
}
