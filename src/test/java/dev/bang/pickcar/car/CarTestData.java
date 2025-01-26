package dev.bang.pickcar.car;

import dev.bang.pickcar.car.entity.CarModel;
import dev.bang.pickcar.car.entity.CarType;
import dev.bang.pickcar.car.entity.FuelType;
import dev.bang.pickcar.car.entity.Segment;
import java.time.LocalDate;
import org.springframework.context.annotation.Profile;

@Profile("test")
public class CarTestData {

    public static final String VALID_CAR_BRAND_NAME = "현대";
    public static final String VALID_CAR_MODEL_NAME = "쏘나타";
    public static final String VALID_CAR_GENERATION = "DN8";
    public static final Segment VALID_CAR_SEGMENT = Segment.D;
    public static final CarType VALID_CAR_TYPE = CarType.SEDAN;
    public static final FuelType VALID_FUEL_TYPE = FuelType.GASOLINE;
    public static final int VALID_CAR_SEAT_CAPACITY = 5;
    public static final int VALID_CAR_DEFAULT_HOUR_RATE = 10_000;
    public static final CarModel VALID_CAR_MODEL = new CarModel(
            VALID_CAR_BRAND_NAME,
            VALID_CAR_MODEL_NAME,
            VALID_CAR_GENERATION,
            VALID_CAR_SEGMENT,
            VALID_CAR_TYPE,
            VALID_FUEL_TYPE,
            VALID_CAR_SEAT_CAPACITY,
            VALID_CAR_DEFAULT_HOUR_RATE
    );

    public static final String VALID_CAR_COLOR = "black";
    public static final String VALID_CAR_VIN = "12345678901234567";
    public static final String VALID_CAR_NUMBER = "123하4567";
    public static final LocalDate VALID_CAR_REGISTRATION_DATE = LocalDate.of(2025, 1, 1);
    public static final int VALID_CAR_MILEAGE = 10000;
    public static final float VALID_CAR_FUEL_LEVEL = 100.0f;
    public static final int VALID_CAR_HOUR_RATE = 20_000;

    private CarTestData() {
    }
}
