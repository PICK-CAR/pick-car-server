package dev.bang.pickcar.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record PhoneNumberRequest(
        @NotBlank(message = "전화번호를 입력해주세요.")
        String phoneNumber
) {
}
