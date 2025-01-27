package dev.bang.pickcar.reservation.service;

import dev.bang.pickcar.car.entity.Car;
import dev.bang.pickcar.car.repository.CarRepository;
import dev.bang.pickcar.member.entity.Member;
import dev.bang.pickcar.member.repository.MemberRepository;
import dev.bang.pickcar.payment.dto.RequestPaymentCancel;
import dev.bang.pickcar.payment.dto.RequestPaymentConfirm;
import dev.bang.pickcar.payment.dto.ResponsePaymentCancel;
import dev.bang.pickcar.payment.dto.ResponsePaymentConfirm;
import dev.bang.pickcar.payment.entity.Payment;
import dev.bang.pickcar.payment.external.PaymentClient;
import dev.bang.pickcar.payment.repository.PaymentRepository;
import dev.bang.pickcar.reservation.dto.RequestReservation;
import dev.bang.pickcar.reservation.entity.Reservation;
import dev.bang.pickcar.reservation.repository.ReservationRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final CarRepository carRepository;
    private final PaymentClient paymentClient;
    private final PaymentRepository paymentRepository;

    @Transactional
    public long createReservation(Long memberId, RequestReservation requestReservation) {
        Member member = findMemberById(memberId);
        Car car = findCarById(requestReservation.carId());
        validatePossibleReservation(car, requestReservation);
        return reservationRepository.save(Reservation.builder()
                .member(member)
                .car(car)
                .startDateTime(requestReservation.startDate())
                .endDateTime(requestReservation.endDate())
                .build()).getId();
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    private Car findCarById(Long carId) {
        return carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 차량입니다."));
    }

    private void validatePossibleReservation(Car car, RequestReservation requestReservation) {
        checkCarAvailable(car);
        checkReservationWithinOneMonth(requestReservation.startDate());
        checkReservationOverlap(car, requestReservation.startDate(), requestReservation.endDate());
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
        boolean isOverLapping = reservationRepository.existsOverlappingReservation(car, start, end);
        if (isOverLapping) {
            throw new IllegalArgumentException("이미 예약된 시간대입니다.");
        }
    }

    @Transactional
    public void payReservation(Long memberId, Long reservationId, RequestPaymentConfirm requestPaymentConfirm) {
        Reservation reservation = findReservationByIdAndMemberId(reservationId, memberId);
        ResponsePaymentConfirm responsePaymentConfirm = paymentClient.confirmPayment(requestPaymentConfirm);
        Payment payment = responsePaymentConfirm.toPayment(reservation);
        checkPaid(payment);
        reservation.confirm();
        paymentRepository.save(payment);
    }

    private void checkPaid(Payment payment) {
        if (!payment.isPaid()) {
            throw new IllegalArgumentException("완료되지 않은 결제입니다.");
        }
    }

    @Transactional
    public void cancelReservation(Long memberId, Long reservationId, RequestPaymentCancel requestPaymentCancel) {
        Reservation reservation = findReservationByIdAndMemberId(reservationId, memberId);
        checkReservationStatus(reservation);

        Payment payment = findPaymentByReservationId(reservationId);
        checkPaid(payment);

        reservation.cancel();
        ResponsePaymentCancel response = paymentClient.cancelPayment(payment.getPaymentKey(), requestPaymentCancel);
        payment.cancel(response.requestedAt(), response.approvedAt());
    }

    private void checkReservationStatus(Reservation reservation) {
        if (reservation.isPending()) {
            throw new IllegalArgumentException("확정되지 않은 예약은 취소할 수 없습니다.");
        }
        if (reservation.isCancelled()) {
            throw new IllegalArgumentException("이미 취소된 예약입니다.");
        }
        if (reservation.isCompleted()) {
            throw new IllegalArgumentException("이미 완료된 예약입니다.");
        }
    }

    private Payment findPaymentByReservationId(Long reservationId) {
        return paymentRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("결제 정보가 존재하지 않습니다."));
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
