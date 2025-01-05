package dev.bang.pickcar.car.repository;

import dev.bang.pickcar.car.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    boolean existsByVin(@Param("vin") String vin);
}
