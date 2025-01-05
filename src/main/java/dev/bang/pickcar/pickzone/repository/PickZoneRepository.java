package dev.bang.pickcar.pickzone.repository;

import dev.bang.pickcar.pickzone.entity.Location;
import dev.bang.pickcar.pickzone.entity.PickZone;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
              AND (:location IS NULL OR p.location = :location)
            """)
    Slice<PickZone> searchPickZones(@Param("keyword") String keyword,
                                    @Param("location") Location location,
                                    Pageable pageable);
}
