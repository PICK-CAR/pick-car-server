package dev.bang.pickcar.payment.dto;

import java.time.ZonedDateTime;

public record ResponsePaymentCancel(
        String paymentKey,
        String orderId,
        String orderName,
        ZonedDateTime requestedAt,
        ZonedDateTime approvedAt,
        String status
) {
}
