package dev.bang.pickcar.reservation.repository;

import static dev.bang.pickcar.reservation.entity.QReservation.reservation;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.bang.pickcar.car.entity.Car;
import dev.bang.pickcar.reservation.entity.ReservationStatus;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReservationQueryRepository {

    private final JPAQueryFactory queryFactory;

    public boolean existsOverlappingReservation(Car car, LocalDateTime start, LocalDateTime end) {
        return queryFactory
                .selectOne()
                .from(reservation)
                .where(
                        reservation.car.eq(car),
                        isNotDeleted(),
                        isNotCancelledOrCompleted(),
                        isTimeOverlapping(start, end)
                )
                .fetchFirst() != null;
    }

    private BooleanExpression isNotDeleted() {
        return reservation.isDeleted.isFalse();
    }

    private BooleanExpression isNotCancelledOrCompleted() {
        return reservation.status.ne(ReservationStatus.CANCELLED)
                .and(reservation.status.ne(ReservationStatus.COMPLETED));
    }

    private BooleanExpression isTimeOverlapping(LocalDateTime start, LocalDateTime end) {
        return reservation.startDateTime.lt(end)
                .and(reservation.endDateTime.goe(start));
    }
}
