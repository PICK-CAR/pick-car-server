package dev.bang.pickcar.auth.dto;

import static dev.bang.pickcar.member.MemberConstant.EMAIL_REGEX;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(
        @Pattern(regexp = EMAIL_REGEX, message = "이메일 형식에 맞게 입력해주세요.")
        String email,
        @NotBlank(message = "비밀번호를 입력해주세요.")
        String password
) {
}
