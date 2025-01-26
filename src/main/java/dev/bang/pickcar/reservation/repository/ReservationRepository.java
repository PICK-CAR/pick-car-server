package dev.bang.pickcar.reservation.repository;

import dev.bang.pickcar.car.entity.Car;
import dev.bang.pickcar.reservation.entity.Reservation;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
            SELECT COUNT(r) > 0
            FROM Reservation r
            WHERE r.car = :car
            AND r.isDeleted = false
            AND (r.status <> 'CANCELLED' AND r.status <> 'COMPLETED')
            AND (
                    (:start < r.endDateTime AND :end >= r.startDateTime)
                    OR (:end > r.startDateTime AND :end <= r.endDateTime)
                    OR (:start <= r.startDateTime AND :end >= r.endDateTime))
            """)
    boolean existsOverlappingReservation(Car car, LocalDateTime start, LocalDateTime end);

    @EntityGraph(attributePaths = {"car"})
    Optional<Reservation> findByIdAndMemberId(Long reservationId, Long memberId);
}
