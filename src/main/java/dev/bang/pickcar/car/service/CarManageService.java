package dev.bang.pickcar.car.service;

import dev.bang.pickcar.car.dto.CarRequest;
import dev.bang.pickcar.car.dto.PickZoneAssignRequest;
import dev.bang.pickcar.car.entity.Car;
import dev.bang.pickcar.car.entity.CarModel;
import dev.bang.pickcar.car.repository.CarModelRepository;
import dev.bang.pickcar.car.repository.CarRepository;
import dev.bang.pickcar.pickzone.entity.PickZone;
import dev.bang.pickcar.pickzone.repository.PickZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CarManageService {

    private final CarRepository carRepository;
    private final CarModelRepository carModelRepository;
    private final PickZoneRepository pickZoneRepository;

    @Transactional
    public long addCar(CarRequest carRequest) {
        checkDuplicateCar(carRequest.vin());
        Car car = requestToCar(carRequest);
        return carRepository.save(car).getId();
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

    @Transactional
    public void deleteCar(Long carId) {
        Car car = findCarById(carId);
        carRepository.delete(car);
    }

    private Car findCarById(Long carId) {
        return carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("차량이 존재하지 않습니다."));
    }

    @Transactional
    public void assignCarToPickZone(Long carId, PickZoneAssignRequest pickZoneAssignRequest) {
        Car car = findCarById(carId);
        PickZone pickZone = findPickZoneById(pickZoneAssignRequest.pickZoneId());
        car.assignPickZone(pickZone);
    }

    private PickZone findPickZoneById(long pickZoneId) {
        return pickZoneRepository.findById(pickZoneId)
                .orElseThrow(() -> new IllegalArgumentException("픽존이 존재하지 않습니다."));
    }
}
