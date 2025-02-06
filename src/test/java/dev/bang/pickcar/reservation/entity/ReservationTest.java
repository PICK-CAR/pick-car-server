package dev.bang.pickcar.reservation.entity;

import static dev.bang.pickcar.reservation.ReservationTestData.VALID_CAR;
import static dev.bang.pickcar.reservation.ReservationTestData.VALID_END_DATE;
import static dev.bang.pickcar.reservation.ReservationTestData.VALID_MEMBER;
import static dev.bang.pickcar.reservation.ReservationTestData.VALID_START_DATE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("공유 차량 예약 테스트")
class ReservationTest {

    @DisplayName("정상적인 공유 차량 예약 생성은 예외가 발생하지 않는다.")
    @Test
    void shouldNotThrowException_WhenValidCreate() {
        assertThatCode(() ->
                Reservation.builder()
                        .member(VALID_MEMBER)
                        .car(VALID_CAR)
                        .startDateTime(VALID_START_DATE)
                        .endDateTime(VALID_END_DATE)
                        .build()
        ).doesNotThrowAnyException();
    }

    @DisplayName("예약 생성 시 회원이 null인 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenMemberIsNull() {
        assertThatThrownBy(() -> Reservation.builder()
                .member(null)
                .car(VALID_CAR)
                .startDateTime(VALID_START_DATE)
                .endDateTime(VALID_END_DATE)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예약 생성 시 차량이 null인 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenCarIsNull() {
        assertThatThrownBy(() -> Reservation.builder()
                .member(VALID_MEMBER)
                .car(null)
                .startDateTime(VALID_START_DATE)
                .endDateTime(VALID_END_DATE)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예약 생성 시 시작 시간이 null인 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenStartDateIsNull() {
        assertThatThrownBy(() -> Reservation.builder()
                .member(VALID_MEMBER)
                .car(VALID_CAR)
                .startDateTime(null)
                .endDateTime(VALID_END_DATE)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예약 생성 시 종료 시간이 null인 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenEndDateIsNull() {
        assertThatThrownBy(() -> Reservation.builder()
                .member(VALID_MEMBER)
                .car(VALID_CAR)
                .startDateTime(VALID_START_DATE)
                .endDateTime(null)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예약 시작 시간이 과거인 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenStartDateIsPast() {
        assertThatThrownBy(() -> Reservation.builder()
                .member(VALID_MEMBER)
                .car(VALID_CAR)
                .startDateTime(LocalDateTime.now().minusDays(1))
                .endDateTime(VALID_END_DATE)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예약 종료 시간이 시작 시간보다 이전인 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenEndDateIsBeforeStartDate() {
        assertThatThrownBy(() -> Reservation.builder()
                .member(VALID_MEMBER)
                .car(VALID_CAR)
                .startDateTime(VALID_START_DATE)
                .endDateTime(VALID_START_DATE.minusHours(1))
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예약이 생성되면 상태는 대기 중으로 설정된다.")
    @Test
    void shouldSetPendingStatus_WhenCreate() {
        Reservation reservation = Reservation.builder()
                .member(VALID_MEMBER)
                .car(VALID_CAR)
                .startDateTime(VALID_START_DATE)
                .endDateTime(VALID_END_DATE)
                .build();

        assertThat(reservation.isPending()).isTrue();
    }

    @DisplayName("대기 중인 예약은 확정할 수 있다.")
    @Test
    void shouldConfirm_WhenPending() {
        Reservation reservation = Reservation.builder()
                .member(VALID_MEMBER)
                .car(VALID_CAR)
                .startDateTime(VALID_START_DATE)
                .endDateTime(VALID_END_DATE)
                .build();

        reservation.confirm();

        assertThat(reservation.isPending()).isFalse();
    }

    @DisplayName("대기 중인 예약을 취소할 수 있다.")
    @Test
    void shouldCancel_WhenPending() {
        Reservation reservation = Reservation.builder()
                .member(VALID_MEMBER)
                .car(VALID_CAR)
                .startDateTime(VALID_START_DATE)
                .endDateTime(VALID_END_DATE)
                .build();

        reservation.cancel();

        assertThat(reservation.isCancelled()).isTrue();
    }

    @DisplayName("확정된 예약은 반납 완료할 수 있다.")
    @Test
    void shouldComplete_WhenConfirmed() {
        Reservation reservation = Reservation.builder()
                .member(VALID_MEMBER)
                .car(VALID_CAR)
                .startDateTime(VALID_START_DATE)
                .endDateTime(VALID_END_DATE)
                .build();

        reservation.confirm();
        reservation.complete();

        assertThat(reservation.isCompleted()).isTrue();
    }

    @DisplayName("대기 중인 예약만 확정할 수 있다.")
    @Test
    void shouldThrowException_WhenConfirmNotPending() {
        Reservation reservation = Reservation.builder()
                .member(VALID_MEMBER)
                .car(VALID_CAR)
                .startDateTime(VALID_START_DATE)
                .endDateTime(VALID_END_DATE)
                .build();

        reservation.confirm();

        assertThatThrownBy(reservation::confirm)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 취소된 예약은 다시 취소할 수 없다.")
    @Test
    void shouldThrowException_WhenCancelAlreadyCancelled() {
        Reservation reservation = Reservation.builder()
                .member(VALID_MEMBER)
                .car(VALID_CAR)
                .startDateTime(VALID_START_DATE)
                .endDateTime(VALID_END_DATE)
                .build();

        reservation.cancel();

        assertThatThrownBy(reservation::cancel)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("확정된 예약이 아닌 경우 반납 완료할 수 없다.")
    @Test
    void shouldThrowException_WhenCompleteNotConfirmed() {
        Reservation reservation = Reservation.builder()
                .member(VALID_MEMBER)
                .car(VALID_CAR)
                .startDateTime(VALID_START_DATE)
                .endDateTime(VALID_END_DATE)
                .build();

        reservation.cancel();

        assertThatThrownBy(reservation::complete)
                .isInstanceOf(IllegalArgumentException.class);
    }
}
