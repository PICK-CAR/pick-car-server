package dev.bang.pickcar.payment.entity;

/**
 * @see <a href="https://docs.tosspayments.com/reference#%EA%B0%9D%EC%B2%B4-%EC%83%81%EC%84%B8">Toss 결제 상태</a>
 */
public enum PaymentStatus {
    READY,
    DONE,
    CANCELED,
    PARTIAL_CANCELED,
    ABORTED,
    EXPIRED,
    ;

    public static PaymentStatus fromString(String status) {
        try {
            return PaymentStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("지원하지 않는 결제 상태입니다.");
        }
    }
}
