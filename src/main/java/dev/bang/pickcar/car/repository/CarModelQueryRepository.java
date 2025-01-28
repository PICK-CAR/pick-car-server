package dev.bang.pickcar.car.repository;

import static dev.bang.pickcar.car.entity.QCarModel.carModel;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.bang.pickcar.car.entity.FuelType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CarModelQueryRepository {

    private final JPAQueryFactory queryFactory;

    public boolean existsSameCarModel(String brand, String name, String generation, FuelType fuelType) {
        return queryFactory
                .from(carModel)
                .where(
                        isSameCarModel(brand, name, generation, fuelType)
                )
                .fetchFirst() != null;
    }

    private BooleanExpression isSameCarModel(String brand, String name, String generation, FuelType fuelType) {
        return carModel.brand.eq(brand)
                .and(carModel.name.eq(name))
                .and(carModel.generation.eq(generation))
                .and(carModel.fuelType.eq(fuelType));
    }
}
