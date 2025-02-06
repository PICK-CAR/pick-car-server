package dev.bang.pickcar.reservation.entity;

import dev.bang.pickcar.car.entity.Car;
import dev.bang.pickcar.global.entitiy.BaseTimeEntity;
import dev.bang.pickcar.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.util.Assert;

@Table(name = "reservations")
@Entity
@SQLDelete(sql = "UPDATE reservations SET is_deleted = true WHERE reservation_id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private boolean isDeleted = Boolean.FALSE;

    @Builder
    public Reservation(Member member,
                       Car car,
                       LocalDateTime startDateTime,
                       LocalDateTime endDateTime) {
        validate(member, car, startDateTime, endDateTime);
        this.member = member;
        this.car = car;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.status = ReservationStatus.PENDING;
    }

    private void validate(Member member, Car car, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Assert.notNull(member, "회원 정보는 필수입니다.");
        Assert.notNull(car, "공유 차량 정보는 필수입니다.");
        Assert.notNull(startDateTime, "대여 시작 일시는 필수입니다.");
        Assert.notNull(endDateTime, "대여 종료 일시는 필수입니다.");
        validateReservationDateTime(startDateTime, endDateTime);
    }

    private void validateReservationDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime.isAfter(endDateTime)) {
            throw new IllegalArgumentException("대여 시작 일시는 대여 종료 일시보다 이전이어야 합니다.");
        }
        if (startDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("대여 시작 일시는 현재 시각 이후여야 합니다.");
        }
    }

    public boolean isPending() {
        return status == ReservationStatus.PENDING;
    }

    public boolean isCancelled() {
        return status == ReservationStatus.CANCELLED;
    }

    public boolean isCompleted() {
        return status == ReservationStatus.COMPLETED;
    }

    public void confirm() {
        if (status != ReservationStatus.PENDING) {
            throw new IllegalArgumentException("대기 중인 예약만 확정할 수 있습니다.");
        }
        status = ReservationStatus.CONFIRMED;
    }

    public void cancel() {
        if (status == ReservationStatus.CANCELLED) {
            throw new IllegalArgumentException("이미 취소된 예약입니다.");
        }
        status = ReservationStatus.CANCELLED;
    }

    public void complete() {
        if (status != ReservationStatus.CONFIRMED) {
            throw new IllegalArgumentException("확정된 예약만 반납 완료할 수 있습니다.");
        }
        status = ReservationStatus.COMPLETED;
    }

    public void validateStatusForCancellation() {
        if (isPending()) {
            throw new IllegalArgumentException("확정되지 않은 예약은 취소할 수 없습니다.");
        }
        if (isCancelled()) {
            throw new IllegalArgumentException("이미 취소된 예약입니다.");
        }
        if (isCompleted()) {
            throw new IllegalArgumentException("이미 완료된 예약입니다.");
        }
    }

    public int calculateTotalPrice() {
        return car.getHourlyRate() * (int) startDateTime.until(endDateTime, ChronoUnit.HOURS);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return Objects.equals(member, that.member) && Objects.equals(car, that.car);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, car);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", member=" + member +
                ", car=" + car +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", status=" + status +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
