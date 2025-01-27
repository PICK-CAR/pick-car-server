package dev.bang.pickcar.payment.dto;

import dev.bang.pickcar.payment.entity.Payment;
import dev.bang.pickcar.payment.entity.PaymentStatus;
import dev.bang.pickcar.reservation.entity.Reservation;
import java.time.ZonedDateTime;

public record ResponsePaymentConfirm(
        String paymentKey,
        String orderId,
        String orderName,
        int totalAmount,
        ZonedDateTime requestedAt,
        ZonedDateTime approvedAt,
        String status
) {
    public Payment toPayment(Reservation reservation) {
        return Payment.builder()
                .paymentKey(paymentKey)
                .orderId(orderId)
                .totalAmount(totalAmount)
                .orderName(orderName)
                .reservation(reservation)
                .requestedAt(requestedAt)
                .approvedAt(approvedAt)
                .status(PaymentStatus.fromString(status))
                .build();
    }
}
