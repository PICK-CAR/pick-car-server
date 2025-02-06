package dev.bang.pickcar.payment;

import java.time.ZonedDateTime;

public class PaymentTestData {

    public static final String PAYMENT_KEY = "paymentKey";
    public static final String ORDER_ID = "orderId";
    public static final String ORDER_NAME = "orderName";
    public static final ZonedDateTime VALID_REQUEST_AT = ZonedDateTime.now();
    public static final ZonedDateTime VALID_APPROVED_AT = ZonedDateTime.now().plusSeconds(1);

    private PaymentTestData() {
    }
}
