package dev.bang.pickcar.reservation.repository;

import dev.bang.pickcar.reservation.entity.Reservation;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @EntityGraph(attributePaths = {"car", "member"})
    Optional<Reservation> findByIdAndMemberId(Long reservationId, Long memberId);
}
