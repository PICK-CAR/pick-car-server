package dev.bang.pickcar.payment.entity;

import dev.bang.pickcar.reservation.entity.Reservation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.util.Assert;

@Table(name = "payments")
@Entity
@SQLDelete(sql = "UPDATE payments SET is_deleted = true WHERE payment_id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "payment_id")
    private Long id;

    private String paymentKey;

    private String orderId;

    private int totalAmount;

    private String orderName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    private ZonedDateTime requestedAt;

    private ZonedDateTime approvedAt;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private boolean isDeleted = Boolean.FALSE;

    @Builder
    public Payment(String paymentKey,
                   String orderId,
                   int totalAmount,
                   String orderName,
                   Reservation reservation,
                   ZonedDateTime requestedAt,
                   ZonedDateTime approvedAt,
                   PaymentStatus status) {
        validatePaymentInfo(paymentKey, orderId, totalAmount);
        validateReservationInfo(reservation);
        validatePaymentTime(requestedAt, approvedAt);
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.orderName = orderName;
        this.reservation = reservation;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
        this.status = status;
    }

    private void validatePaymentInfo(String paymentKey, String orderId, int totalAmount) {
        Assert.hasText(paymentKey, "결제 키는 필수입니다.");
        Assert.hasText(orderId, "주문 ID는 필수입니다.");
        Assert.isTrue(totalAmount >= 0, "결제 금액은 0 이상이어야 합니다.");
    }

    private void validateReservationInfo(Reservation reservation) {
        Assert.notNull(reservation, "예약 정보는 필수입니다.");
    }

    private void validatePaymentTime(ZonedDateTime requestedAt, ZonedDateTime approvedAt) {
        Assert.notNull(requestedAt, "요청 시간은 필수입니다.");
        Assert.notNull(approvedAt, "승인 시간은 필수입니다.");
        Assert.isTrue(requestedAt.isBefore(approvedAt), "요청 시간은 승인 시간보다 이전이어야 합니다.");
    }

    public boolean isPaid() {
        return status == PaymentStatus.DONE;
    }

    public void cancel(ZonedDateTime requestedAt, ZonedDateTime approvedAt) {
        validatePaymentTime(requestedAt, approvedAt);
        status = PaymentStatus.CANCELED;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
    }

    public boolean isCancelable() {
        return status == PaymentStatus.DONE;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Payment payment = (Payment) o;
        return Objects.equals(paymentKey, payment.paymentKey)
                && Objects.equals(orderId, payment.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentKey, orderId);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", paymentKey='" + paymentKey + '\'' +
                ", orderId='" + orderId + '\'' +
                ", totalAmount=" + totalAmount +
                ", orderName='" + orderName + '\'' +
                ", reservation=" + reservation +
                ", requestedAt=" + requestedAt +
                ", approvedAt=" + approvedAt +
                ", status=" + status +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
