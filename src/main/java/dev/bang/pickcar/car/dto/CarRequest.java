package dev.bang.pickcar.car.dto;

import static dev.bang.pickcar.member.MemberConstant.LOCAL_DATE_FORMAT;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

public record CarRequest(
        long carModelId,
        @NotBlank(message = "색상을 입력해주세요.")
        String color,
        @NotBlank(message = "차대번호를 입력해주세요.")
        String vin,
        @NotBlank(message = "차량 번호를 입력해주세요.")
        String licensePlate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = LOCAL_DATE_FORMAT, timezone = "Asia/Seoul")
        @Past(message = "등록일은 현재보다 이전 날짜여야 합니다.")
        LocalDate registrationDate,
        @NotNull(message = "주행거리를 입력해주세요.")
        Integer mileage,
        @NotNull(message = "연료량을 입력해주세요.")
        Float fuelLevel,
        Integer hourlyRate
) {
}
