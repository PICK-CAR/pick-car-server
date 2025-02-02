package dev.bang.pickcar.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record EmailVerifyRequest(
        @NotBlank(message = "이메일을 입력해주세요.")
        String email,
        @NotBlank(message = "인증번호를 입력해주세요.")
        String verificationCode
) {
}
