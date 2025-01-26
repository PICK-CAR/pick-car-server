package dev.bang.pickcar.car.dto;

import jakarta.validation.constraints.NotNull;

public record CarHourlyRateRequest(
        @NotNull(message = "시간당 요금을 입력해주세요.")
        Integer hourlyRate
) {
}
