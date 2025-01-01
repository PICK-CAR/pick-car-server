package dev.bang.pickcar.car;

import dev.bang.pickcar.car.entity.CarType;
import dev.bang.pickcar.car.entity.FuelType;
import dev.bang.pickcar.car.entity.Segment;

public class CarTestData {

    public static final String VALID_CAR_BRAND_NAME = "현대";
    public static final String VALID_CAR_MODEL_NAME = "쏘나타";
    public static final String VALID_CAR_GENERATION = "DN8";
    public static final Segment VALID_CAR_SEGMENT = Segment.D;
    public static final CarType VALID_CAR_TYPE = CarType.SEDAN;
    public static final FuelType VALID_FUEL_TYPE = FuelType.GASOLINE;
    public static final int VALID_CAR_SEAT_CAPACITY = 5;

    private CarTestData() {
    }
}
