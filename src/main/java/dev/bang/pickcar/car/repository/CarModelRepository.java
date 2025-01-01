package dev.bang.pickcar.car.repository;

import dev.bang.pickcar.car.entity.CarModel;
import dev.bang.pickcar.car.entity.FuelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CarModelRepository extends JpaRepository<CarModel, Long> {

    @Query("""
            SELECT CASE WHEN COUNT(cm) > 0 THEN TRUE ELSE FALSE END
            FROM CarModel cm
            WHERE cm.brand = :brand
              and cm.name = :name
              and cm.generation = :generation
              and cm.fuelType = :fuelType
            """)
    boolean existsSameCarModel(@Param("brand") String brand,
                               @Param("name") String name,
                               @Param("generation") String generation,
                               @Param("fuelType") FuelType fuelType);
}
