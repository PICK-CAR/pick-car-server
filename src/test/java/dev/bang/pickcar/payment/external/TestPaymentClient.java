package dev.bang.pickcar.payment.external;

import static dev.bang.pickcar.payment.PaymentTestData.ORDER_ID;
import static dev.bang.pickcar.payment.PaymentTestData.ORDER_NAME;
import static dev.bang.pickcar.payment.PaymentTestData.PAYMENT_KEY;

import dev.bang.pickcar.payment.dto.RequestPaymentCancel;
import dev.bang.pickcar.payment.dto.RequestPaymentConfirm;
import dev.bang.pickcar.payment.dto.ResponsePaymentCancel;
import dev.bang.pickcar.payment.dto.ResponsePaymentConfirm;
import dev.bang.pickcar.payment.entity.PaymentStatus;
import java.time.ZonedDateTime;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@Component
@ActiveProfiles("test")
public class TestPaymentClient implements PaymentClient {

    @Override
    public ResponsePaymentConfirm confirmPayment(RequestPaymentConfirm confirmRequest) {
        ZonedDateTime now = ZonedDateTime.now();
        return new ResponsePaymentConfirm(
                PAYMENT_KEY,
                ORDER_ID,
                ORDER_NAME,
                1_000,
                now.plusDays(1),
                now.plusDays(2),
                PaymentStatus.DONE.name()
        );
    }

    @Override
    public ResponsePaymentCancel cancelPayment(String paymentKey, RequestPaymentCancel cancelRequest) {
        ZonedDateTime now = ZonedDateTime.now();
        return new ResponsePaymentCancel(
                PAYMENT_KEY,
                ORDER_ID,
                ORDER_NAME,
                now.plusDays(1),
                now.plusDays(2),
                PaymentStatus.CANCELED.name()
        );
    }
}
