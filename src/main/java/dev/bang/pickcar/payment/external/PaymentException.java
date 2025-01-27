package dev.bang.pickcar.payment.external;

public class PaymentException extends RuntimeException {

    public PaymentException(String message) {
        super(message);
    }
}
