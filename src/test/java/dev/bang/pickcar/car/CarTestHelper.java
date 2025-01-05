package dev.bang.pickcar.car;

import static dev.bang.pickcar.car.CarTestData.VALID_CAR_BRAND_NAME;
import static dev.bang.pickcar.car.CarTestData.VALID_CAR_GENERATION;
import static dev.bang.pickcar.car.CarTestData.VALID_CAR_MODEL;
import static dev.bang.pickcar.car.CarTestData.VALID_CAR_MODEL_NAME;
import static dev.bang.pickcar.car.CarTestData.VALID_CAR_SEAT_CAPACITY;
import static dev.bang.pickcar.car.CarTestData.VALID_CAR_SEGMENT;
import static dev.bang.pickcar.car.CarTestData.VALID_CAR_TYPE;
import static dev.bang.pickcar.car.CarTestData.VALID_FUEL_TYPE;

import dev.bang.pickcar.car.dto.CarModelRequest;

import dev.bang.pickcar.car.dto.CarRequest;
import dev.bang.pickcar.car.entity.Car;
import dev.bang.pickcar.car.entity.CarModel;
import dev.bang.pickcar.car.repository.CarModelRepository;
import dev.bang.pickcar.car.repository.CarRepository;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@Component
@ActiveProfiles("test")
public class CarTestHelper {

    @Autowired
    private CarModelRepository carModelRepository;

    @Autowired
    private CarRepository carRepository;

    private final AtomicLong counter = new AtomicLong();

    public CarModelRequest createCarModelRequest() {
        return new CarModelRequest(
                VALID_CAR_BRAND_NAME,
                VALID_CAR_MODEL_NAME,
                VALID_CAR_GENERATION,
                VALID_CAR_SEGMENT.name(),
                VALID_CAR_TYPE.name(),
                VALID_FUEL_TYPE.name(),
                VALID_CAR_SEAT_CAPACITY
        );
    }

    public CarModelRequest createCustomCarModelRequest(Object... args) {
        return new CarModelRequest(
                (String) args[0],
                (String) args[1],
                (String) args[2],
                (String) args[3],
                (String) args[4],
                (String) args[5],
                (int) args[6]
        );
    }

    public CarModel createCarModel() {
        String uniqueCarModelName = VALID_CAR_MODEL_NAME + counter.incrementAndGet();
        CarModelRequest carModelRequest = createCustomCarModelRequest(
                VALID_CAR_BRAND_NAME,
                uniqueCarModelName,
                VALID_CAR_GENERATION,
                VALID_CAR_SEGMENT.name(),
                VALID_CAR_TYPE.name(),
                VALID_FUEL_TYPE.name(),
                VALID_CAR_SEAT_CAPACITY
        );
        return carModelRepository.save(carModelRequest.toCarModel());
    }

    public CarModel createCarModel(CarModelRequest carModelRequest) {
        return carModelRepository.save(carModelRequest.toCarModel());
    }

    public CarRequest createCarRequest(long carModelId) {
        return new CarRequest(
                carModelId,
                CarTestData.VALID_CAR_COLOR,
                CarTestData.VALID_CAR_VIN,
                CarTestData.VALID_CAR_NUMBER,
                CarTestData.VALID_CAR_REGISTRATION_DATE,
                CarTestData.VALID_CAR_MILEAGE,
                CarTestData.VALID_CAR_FUEL_LEVEL
        );
    }

    public Car createCar(CarRequest carRequest) {
        return carRepository.save(Car.builder()
                .model(createCarModel())
                .color(carRequest.color())
                .vin(carRequest.vin())
                .licensePlate(carRequest.licensePlate())
                .registrationDate(carRequest.registrationDate())
                .mileage(carRequest.mileage())
                .fuelLevel(carRequest.fuelLevel())
                .build()
        );
    }
}
