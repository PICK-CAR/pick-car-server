package dev.bang.pickcar.reservation.service;

import dev.bang.pickcar.car.entity.Car;
import dev.bang.pickcar.member.entity.Member;
import dev.bang.pickcar.payment.dto.RequestPaymentCancel;
import dev.bang.pickcar.payment.dto.RequestPaymentConfirm;
import dev.bang.pickcar.payment.entity.Payment;
import dev.bang.pickcar.payment.service.PaymentService;
import dev.bang.pickcar.reservation.entity.Reservation;
import dev.bang.pickcar.reservation.repository.ReservationQueryRepository;
import dev.bang.pickcar.reservation.repository.ReservationRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationQueryRepository reservationQueryRepository;
    private final PaymentService paymentService;

    @Transactional
    public long createReservation(Member member, Car car, LocalDateTime start, LocalDateTime end) {
        validatePossibleReservation(car, start, end);
        return reservationRepository.save(Reservation.builder()
                .member(member)
                .car(car)
                .startDateTime(start)
                .endDateTime(end)
                .build()).getId();
    }

    private void validatePossibleReservation(Car car, LocalDateTime start, LocalDateTime end) {
        checkCarAvailable(car);
        checkReservationWithinOneMonth(start);
        checkReservationOverlap(car, start, end);
    }

    private void checkCarAvailable(Car car) {
        if (!car.isAvailable()) {
            throw new IllegalArgumentException("대여 가능한 차량이 아닙니다.");
        }
    }

    private void checkReservationWithinOneMonth(LocalDateTime start) {
        LocalDateTime oneMonthLater = LocalDateTime.now().plusMonths(1);
        if (start.isAfter(oneMonthLater)) {
            throw new IllegalArgumentException("예약은 1달 이내만 가능합니다.");
        }
    }

    private void checkReservationOverlap(Car car, LocalDateTime start, LocalDateTime end) {
        boolean isOverLapping = reservationQueryRepository.existsOverlappingReservation(car, start, end);
        if (isOverLapping) {
            throw new IllegalArgumentException("이미 예약된 시간대입니다.");
        }
    }

    @Transactional
    public void payReservation(Long memberId, Long reservationId, RequestPaymentConfirm confirmRequest) {
        Reservation reservation = findReservationByIdAndMemberId(reservationId, memberId);
        paymentService.confirmPayment(reservation, confirmRequest);
        reservation.confirm();
    }

    @Transactional
    public void cancelReservation(Long memberId, Long reservationId, RequestPaymentCancel cancelRequest) {
        Reservation reservation = findReservationByIdAndMemberId(reservationId, memberId);
        checkReservationStatusForCancellation(reservation);

        Payment payment = paymentService.findByReservationId(reservationId);
        paymentService.cancelPayment(payment, cancelRequest);
        reservation.cancel();
    }

    private void checkReservationStatusForCancellation(Reservation reservation) {
        reservation.validateStatusForCancellation();
    }

    @Transactional
    public void completeReturn(Long memberId, Long reservationId) {
        Reservation reservation = findReservationByIdAndMemberId(reservationId, memberId);
        reservation.complete();
    }

    private Reservation findReservationByIdAndMemberId(Long reservationId, Long memberId) {
        return reservationRepository.findByIdAndMemberId(reservationId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약이거나 해당 회원의 예약이 아닙니다."));
    }
}
