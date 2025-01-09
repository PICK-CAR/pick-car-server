package dev.bang.pickcar.pickzone.repository;

import dev.bang.pickcar.pickzone.dto.PickZoneMarker;
import dev.bang.pickcar.pickzone.entity.PickZone;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PickZoneRepository extends JpaRepository<PickZone, Long> {

    @Query("""
            SELECT p
            FROM PickZone p
            WHERE (:keyword IS NULL OR p.name LIKE %:keyword%)
            """)
    Slice<PickZone> searchPickZones(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
            SELECT new dev.bang.pickcar.pickzone.dto.PickZoneMarker(p.id, p.name, p.location.latitude, p.location.longitude)
            FROM PickZone p
            WHERE p.location.latitude BETWEEN :south AND :north
              AND p.location.longitude BETWEEN :west AND :east
            """)
    List<PickZoneMarker> findPickZonesNearby(@Param("south") double south,
                                             @Param("north") double north,
                                             @Param("west") double west,
                                             @Param("east") double east);

    @EntityGraph(attributePaths = {"cars", "cars.model"})
    Optional<PickZone> findPickZoneWithCarsById(Long pickZoneId);
}
