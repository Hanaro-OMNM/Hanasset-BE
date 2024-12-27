package com.omnm.hanasset.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Schema(description = "생년월일 입력 요청")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BirthRequest {
    @Schema(description = "로그인 이메일", example = "test@example.com")
    @NotBlank
    private String email;

    @Schema(description = "생년월일", example = "1991-10-31")
    @NotNull(message = "생년월일을 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
}
