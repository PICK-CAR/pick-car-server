package dev.bang.pickcar.reservation.entity;

/**
 * PENDING: 예약 요청이 만들어졌지만 아직 확정되지 않은 상태.
 * CONFIRMED: 예약이 승인된 상태.
 * CANCELLED: 사용자가 예약을 취소한 상태.
 * COMPLETED: 예약이 완료된 상태. (차량 반납 완료)
 */
public enum ReservationStatus {

    PENDING,
    CONFIRMED,
    CANCELLED,
    COMPLETED,
}
