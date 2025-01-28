package dev.bang.pickcar.reservation.controller;

import static dev.bang.pickcar.reservation.ReservationConstant.RESERVATION_RESOURCE_LOCATION;

import dev.bang.pickcar.auth.util.LoginMemberId;
import dev.bang.pickcar.payment.dto.RequestPaymentCancel;
import dev.bang.pickcar.payment.dto.RequestPaymentConfirm;
import dev.bang.pickcar.reservation.controller.docs.ReservationApiDocs;
import dev.bang.pickcar.reservation.dto.RequestReservation;
import dev.bang.pickcar.reservation.service.ReservationFacade;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("reservations")
public class ReservationController implements ReservationApiDocs {

    private final ReservationFacade reservationFacade;

    @PostMapping
    @PreAuthorize("hasRole('MEMBER')")
    @Override
    public ResponseEntity<Void> createReservation(@LoginMemberId Long memberId,
                                                  @RequestBody @Valid RequestReservation requestReservation) {
        long reservationId = reservationFacade.createReservation(memberId, requestReservation);
        URI resourceUri = URI.create(RESERVATION_RESOURCE_LOCATION + reservationId);
        return ResponseEntity.created(resourceUri).build();
    }

    @PostMapping("{reservationId}/payment")
    @PreAuthorize("hasRole('MEMBER')")
    @Override
    public ResponseEntity<Void> payReservation(@LoginMemberId Long memberId,
                                               @PathVariable Long reservationId,
                                               @RequestBody @Valid RequestPaymentConfirm requestPaymentConfirm) {
        reservationFacade.payReservation(memberId, reservationId, requestPaymentConfirm);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{reservationId}/cancel")
    @PreAuthorize("hasRole('MEMBER')")
    @Override
    public ResponseEntity<Void> cancelReservation(@LoginMemberId Long memberId,
                                                  @PathVariable("reservationId") Long reservationId,
                                                  @RequestBody @Valid RequestPaymentCancel requestPaymentCancel) {
        reservationFacade.cancelReservation(memberId, reservationId, requestPaymentCancel);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{reservationId}/return")
    @PreAuthorize("hasRole('MEMBER')")
    @Override
    public ResponseEntity<Void> completeReturn(@LoginMemberId Long memberId,
                                               @PathVariable("reservationId") Long reservationId) {
        reservationFacade.completeReturn(memberId, reservationId);
        return ResponseEntity.ok().build();
    }
}
