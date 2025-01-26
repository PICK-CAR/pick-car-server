package dev.bang.pickcar.payment.external;

import dev.bang.pickcar.payment.dto.RequestPaymentCancel;
import dev.bang.pickcar.payment.dto.RequestPaymentConfirm;
import dev.bang.pickcar.payment.dto.ResponsePaymentCancel;
import dev.bang.pickcar.payment.dto.ResponsePaymentConfirm;

public interface PaymentClient {

    ResponsePaymentConfirm confirmPayment(RequestPaymentConfirm confirmRequest);

    ResponsePaymentCancel cancelPayment(String paymentKey, RequestPaymentCancel cancelRequest);
}
