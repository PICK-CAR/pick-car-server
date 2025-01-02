package dev.bang.pickcar.car.service;

import dev.bang.pickcar.car.dto.CarRequest;
import dev.bang.pickcar.car.entity.Car;
import dev.bang.pickcar.car.entity.CarModel;
import dev.bang.pickcar.car.repository.CarModelRepository;
import dev.bang.pickcar.car.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CarManageService {

    private final CarRepository carRepository;
    private final CarModelRepository carModelRepository;

    @Transactional
    public Long addCar(CarRequest carRequest) {
        checkDuplicateCar(carRequest.vin());
        Car car = requestToCar(carRequest);
        return carRepository.save(car)
                .getId();
    }

    private void checkDuplicateCar(String licensePlate) {
        if (carRepository.existsByVin(licensePlate)) {
            throw new IllegalArgumentException("이미 존재하는 차량입니다.");
        }
    }

    private Car requestToCar(CarRequest carRequest) {
        CarModel carModel = findCarModelById(carRequest.carModelId());
        return Car.builder()
                .model(carModel)
                .color(carRequest.color())
                .vin(carRequest.vin())
                .licensePlate(carRequest.licensePlate())
                .registrationDate(carRequest.registrationDate())
                .mileage(carRequest.mileage())
                .fuelLevel(carRequest.fuelLevel())
                .build();
    }

    private CarModel findCarModelById(long carModelId) {
        return carModelRepository.findById(carModelId)
                .orElseThrow(() -> new IllegalArgumentException("차량 모델이 존재하지 않습니다."));
    }
}
