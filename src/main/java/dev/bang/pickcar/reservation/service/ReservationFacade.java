package dev.bang.pickcar.reservation.service;

import dev.bang.pickcar.car.entity.Car;
import dev.bang.pickcar.car.service.CarService;
import dev.bang.pickcar.member.entity.Member;
import dev.bang.pickcar.member.service.MemberService;
import dev.bang.pickcar.payment.dto.RequestPaymentCancel;
import dev.bang.pickcar.payment.dto.RequestPaymentConfirm;
import dev.bang.pickcar.reservation.dto.RequestReservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationFacade {

    private final ReservationService reservationService;
    private final MemberService memberService;
    private final CarService carService;

    @Transactional
    public long createReservation(Long memberId, RequestReservation requestReservation) {
        Member member = memberService.findById(memberId);
        Car car = carService.findById(requestReservation.carId());
        return reservationService.createReservation(member, car, requestReservation.start(), requestReservation.end());
    }

    @Transactional
    public void payReservation(Long memberId, Long reservationId, RequestPaymentConfirm requestPaymentConfirm) {
        reservationService.payReservation(memberId, reservationId, requestPaymentConfirm);
    }

    @Transactional
    public void cancelReservation(Long memberId, Long reservationId, RequestPaymentCancel requestPaymentCancel) {
        reservationService.cancelReservation(memberId, reservationId, requestPaymentCancel);
    }

    @Transactional
    public void completeReturn(Long memberId, Long reservationId) {
        reservationService.completeReturn(memberId, reservationId);
    }
}
