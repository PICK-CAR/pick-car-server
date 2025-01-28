package dev.bang.pickcar.car;

import static dev.bang.pickcar.car.CarTestData.*;
import static dev.bang.pickcar.car.CarTestData.VALID_CAR_BRAND_NAME;
import static dev.bang.pickcar.car.CarTestData.VALID_CAR_COLOR;
import static dev.bang.pickcar.car.CarTestData.VALID_CAR_GENERATION;
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
                VALID_CAR_SEAT_CAPACITY,
                VALID_CAR_DEFAULT_HOUR_RATE
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
                (int) args[6],
                (int) args[7]
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
                VALID_CAR_SEAT_CAPACITY,
                VALID_CAR_DEFAULT_HOUR_RATE
        );
        return carModelRepository.save(carModelRequest.toCarModel());
    }

    public CarModel createCarModel(CarModelRequest carModelRequest) {
        return carModelRepository.save(carModelRequest.toCarModel());
    }

    public CarRequest createCarRequest(long carModelId) {
        return new CarRequest(
                carModelId,
                VALID_CAR_COLOR,
                createUniqueVin(),
                createUniqueLicensePlate(),
                VALID_CAR_REGISTRATION_DATE,
                VALID_CAR_MILEAGE,
                VALID_CAR_FUEL_LEVEL,
                VALID_CAR_HOUR_RATE
        );
    }

    private String createUniqueVin() {
        // 숫자 17개
        long number = counter.incrementAndGet();
        return String.format("%017d", number);
    }

    private String createUniqueLicensePlate() {
        // 숫자 3개 + "하" + 숫자 4개
        long number = counter.incrementAndGet();
        return String.format("%03d하%04d", number % 1000, number % 10000);
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
                .hourlyRate(carRequest.hourlyRate())
                .build()
        );
    }
}
