package dev.bang.pickcar.reservation.dto;

import static dev.bang.pickcar.reservation.ReservationConstant.LOCAL_DATE_TIME_FORMAT;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record RequestReservation(
        @NotNull(message = "차량 ID를 입력해주세요.")
        Long carId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = LOCAL_DATE_TIME_FORMAT, timezone = "Asia/Seoul")
        LocalDateTime start,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = LOCAL_DATE_TIME_FORMAT, timezone = "Asia/Seoul")
        LocalDateTime end
) {
}
