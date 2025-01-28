package dev.bang.pickcar.payment.service;

import dev.bang.pickcar.payment.dto.RequestPaymentCancel;
import dev.bang.pickcar.payment.dto.RequestPaymentConfirm;
import dev.bang.pickcar.payment.dto.ResponsePaymentCancel;
import dev.bang.pickcar.payment.dto.ResponsePaymentConfirm;
import dev.bang.pickcar.payment.entity.Payment;
import dev.bang.pickcar.payment.external.PaymentClient;
import dev.bang.pickcar.payment.repository.PaymentRepository;
import dev.bang.pickcar.reservation.entity.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentClient paymentClient;
    private final PaymentRepository paymentRepository;

    @Transactional
    public void confirmPayment(Reservation reservation, RequestPaymentConfirm confirmRequest) {
        ResponsePaymentConfirm responsePaymentConfirm = paymentClient.confirmPayment(confirmRequest);
        savePayment(reservation, responsePaymentConfirm);
    }

    private void savePayment(Reservation reservation, ResponsePaymentConfirm responsePaymentConfirm) {
        Payment payment = responsePaymentConfirm.toPayment(reservation);
        checkPaid(payment);
        paymentRepository.save(payment);
    }

    private void checkPaid(Payment payment) {
        if (!payment.isPaid()) {
            throw new IllegalArgumentException("결제가 완료되지 않았습니다.");
        }
    }

    @Transactional
    public void cancelPayment(Payment payment, RequestPaymentCancel cancelRequest) {
        if (!payment.isCancelable()) {
            throw new IllegalArgumentException("결제 취소가 불가능합니다. (이미 취소되었거나 환불 불가능한 상태)");
        }
        ResponsePaymentCancel cancelResponse = paymentClient.cancelPayment(payment.getPaymentKey(), cancelRequest);
        payment.cancel(cancelResponse.requestedAt(), cancelResponse.approvedAt());
    }

    @Transactional(readOnly = true)
    public Payment findByReservationId(Long reservationId) {
        return paymentRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("결제 정보가 존재하지 않습니다."));
    }
}
