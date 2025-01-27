package dev.bang.pickcar.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RequestPaymentConfirm(
        @NotBlank(message = "orderId를 입력해주세요.")
        String orderId,
        @NotNull(message = "amount를 입력해주세요.")
        Integer amount,
        @NotBlank(message = "paymentKey를 입력해주세요.")
        String paymentKey
) {
}
