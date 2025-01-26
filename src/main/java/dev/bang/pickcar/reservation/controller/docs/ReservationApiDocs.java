package dev.bang.pickcar.reservation.controller.docs;

import dev.bang.pickcar.payment.dto.RequestPaymentCancel;
import dev.bang.pickcar.payment.dto.RequestPaymentConfirm;
import dev.bang.pickcar.reservation.dto.RequestReservation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Reservation", description = "공유 차량 예약 API")
public interface ReservationApiDocs {

    @Operation(summary = "예약 생성", description = "공유 차량을 예약합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "예약 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<Void> createReservation(@Parameter(hidden = true) Long memberId,
                                           RequestReservation requestReservation);

    @Operation(summary = "예약 결제", description = "예약을 결제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "예약 확인 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<Void> payReservation(@Parameter(hidden = true) Long memberId,
                                        Long reservationId,
                                        RequestPaymentConfirm requestPaymentConfirm);

    @Operation(summary = "예약 취소", description = "예약을 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "예약 취소 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<Void> cancelReservation(@Parameter(hidden = true) Long memberId,
                                           Long reservationId,
                                           RequestPaymentCancel requestPaymentCancel);

    @Operation(summary = "반납 완료", description = "차량 반납을 완료합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반납 완료 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<Void> completeReturn(@Parameter(hidden = true) Long memberId, Long reservationId);
}
