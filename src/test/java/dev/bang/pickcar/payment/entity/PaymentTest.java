package dev.bang.pickcar.payment.entity;

import static dev.bang.pickcar.payment.PaymentTestData.ORDER_ID;
import static dev.bang.pickcar.payment.PaymentTestData.ORDER_NAME;
import static dev.bang.pickcar.payment.PaymentTestData.PAYMENT_KEY;
import static dev.bang.pickcar.payment.PaymentTestData.VALID_APPROVED_AT;
import static dev.bang.pickcar.payment.PaymentTestData.VALID_REQUEST_AT;
import static dev.bang.pickcar.reservation.ReservationTestData.VALID_RESERVATION;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@DisplayName("결제 테스트")
class PaymentTest {

    @DisplayName("정상적인 결제 객체 생성은 예외가 발생하지 않는다.")
    @Test
    void shouldNotThrowException_WhenValidCreate() {
        assertThatCode(() -> Payment.builder()
                .paymentKey(PAYMENT_KEY)
                .orderId(ORDER_ID)
                .totalAmount(VALID_RESERVATION.calculateTotalPrice())
                .orderName(ORDER_NAME)
                .reservation(VALID_RESERVATION)
                .requestedAt(VALID_REQUEST_AT)
                .approvedAt(VALID_APPROVED_AT)
                .status(PaymentStatus.DONE)
                .build()
        ).doesNotThrowAnyException();
    }

    @DisplayName("결제 객체 생성 시 결제 키가 null 또는 빈 문자열인 경우 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowException_WhenPaymentKeyIsNullOrEmpty(String paymentKey) {
        assertThatThrownBy(() -> Payment.builder()
                .paymentKey(paymentKey)
                .orderId(ORDER_ID)
                .totalAmount(VALID_RESERVATION.calculateTotalPrice())
                .orderName(ORDER_NAME)
                .reservation(VALID_RESERVATION)
                .requestedAt(VALID_REQUEST_AT)
                .approvedAt(VALID_APPROVED_AT)
                .status(PaymentStatus.DONE)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("결제 객체 생성 시 주문 ID가 null 또는 빈 문자열인 경우 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowException_WhenOrderIdIsNullOrEmpty(String orderId) {
        assertThatThrownBy(() -> Payment.builder()
                .paymentKey(PAYMENT_KEY)
                .orderId(orderId)
                .totalAmount(VALID_RESERVATION.calculateTotalPrice())
                .orderName(ORDER_NAME)
                .reservation(VALID_RESERVATION)
                .requestedAt(VALID_REQUEST_AT)
                .approvedAt(VALID_APPROVED_AT)
                .status(PaymentStatus.DONE)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("결제 객체 생성 시 결제 금액이 음수인 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenTotalAmountIsNegative() {
        assertThatThrownBy(() -> Payment.builder()
                .paymentKey(PAYMENT_KEY)
                .orderId(ORDER_ID)
                .totalAmount(-1)
                .orderName(ORDER_NAME)
                .reservation(VALID_RESERVATION)
                .requestedAt(VALID_REQUEST_AT)
                .approvedAt(VALID_APPROVED_AT)
                .status(PaymentStatus.DONE)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("결제 객체 생성 시 예약 객체가 null인 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenReservationIsNull() {
        assertThatThrownBy(() -> Payment.builder()
                .paymentKey(PAYMENT_KEY)
                .orderId(ORDER_ID)
                .totalAmount(VALID_RESERVATION.calculateTotalPrice())
                .orderName(ORDER_NAME)
                .reservation(null)
                .requestedAt(VALID_REQUEST_AT)
                .approvedAt(VALID_APPROVED_AT)
                .status(PaymentStatus.DONE)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("결제 객체 생성 시 요청 시간이 null인 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenRequestedAtIsNull() {
        assertThatThrownBy(() -> Payment.builder()
                .paymentKey(PAYMENT_KEY)
                .orderId(ORDER_ID)
                .totalAmount(VALID_RESERVATION.calculateTotalPrice())
                .orderName(ORDER_NAME)
                .reservation(VALID_RESERVATION)
                .requestedAt(null)
                .approvedAt(VALID_APPROVED_AT)
                .status(PaymentStatus.DONE)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("결제 객체 생성 시 승인 시간이 null인 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenApprovedAtIsNull() {
        assertThatThrownBy(() -> Payment.builder()
                .paymentKey(PAYMENT_KEY)
                .orderId(ORDER_ID)
                .totalAmount(VALID_RESERVATION.calculateTotalPrice())
                .orderName(ORDER_NAME)
                .reservation(VALID_RESERVATION)
                .requestedAt(VALID_REQUEST_AT)
                .approvedAt(null)
                .status(PaymentStatus.DONE)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("결제 객체 생성 시 요청 시간이 승인 시간보다 이전인 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenRequestedAtIsBeforeApprovedAt() {
        assertThatThrownBy(() -> Payment.builder()
                .paymentKey(PAYMENT_KEY)
                .orderId(ORDER_ID)
                .totalAmount(VALID_RESERVATION.calculateTotalPrice())
                .orderName(ORDER_NAME)
                .reservation(VALID_RESERVATION)
                .requestedAt(VALID_REQUEST_AT)
                .approvedAt(VALID_REQUEST_AT.minusSeconds(1))
                .status(PaymentStatus.DONE)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("결제가 완료된 경우 isPaid()는 true를 반환한다.")
    @Test
    void isPaid() {
        Payment payment = Payment.builder()
                .paymentKey(PAYMENT_KEY)
                .orderId(ORDER_ID)
                .totalAmount(VALID_RESERVATION.calculateTotalPrice())
                .orderName(ORDER_NAME)
                .reservation(VALID_RESERVATION)
                .requestedAt(VALID_REQUEST_AT)
                .approvedAt(VALID_APPROVED_AT)
                .status(PaymentStatus.DONE)
                .build();

        assertTrue(payment.isPaid());
    }

    @DisplayName("결제 취소 시 상태가 CANCELED로 변경된다.")
    @Test
    void cancel() {
        Payment payment = Payment.builder()
                .paymentKey(PAYMENT_KEY)
                .orderId(ORDER_ID)
                .totalAmount(VALID_RESERVATION.calculateTotalPrice())
                .orderName(ORDER_NAME)
                .reservation(VALID_RESERVATION)
                .requestedAt(VALID_REQUEST_AT)
                .approvedAt(VALID_APPROVED_AT)
                .status(PaymentStatus.DONE)
                .build();

        payment.cancel(VALID_REQUEST_AT, VALID_APPROVED_AT);

        assertSame(PaymentStatus.CANCELED, payment.getStatus());
    }

    @DisplayName("결제 취소 시 요청 시간이 승인 시간보다 이전인 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenRequestedAtIsBeforeApprovedAtInCancel() {
        Payment payment = Payment.builder()
                .paymentKey(PAYMENT_KEY)
                .orderId(ORDER_ID)
                .totalAmount(VALID_RESERVATION.calculateTotalPrice())
                .orderName(ORDER_NAME)
                .reservation(VALID_RESERVATION)
                .requestedAt(VALID_REQUEST_AT)
                .approvedAt(VALID_APPROVED_AT)
                .status(PaymentStatus.DONE)
                .build();

        assertThatThrownBy(() -> payment.cancel(VALID_REQUEST_AT, VALID_REQUEST_AT.minusSeconds(1)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
