package dev.bang.pickcar.car.service;

import dev.bang.pickcar.car.dto.CarModelRequest;
import dev.bang.pickcar.car.dto.CarModelResponse;
import dev.bang.pickcar.car.entity.CarModel;
import dev.bang.pickcar.car.entity.FuelType;
import dev.bang.pickcar.car.repository.CarModelRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CarModelManageService {

    private final CarModelRepository carModelRepository;

    @Transactional
    public Long addCarModel(CarModelRequest carModelRequest) {
        checkDuplicateCarModel(carModelRequest.brand(), carModelRequest.name(), carModelRequest.generation(), carModelRequest.fuelType());
        CarModel carModel = carModelRequest.toCarModel();
        return carModelRepository.save(carModel)
                .getId();
    }

    private void checkDuplicateCarModel(String brand, String name, String generation, String fuelTypeValue) {
        FuelType fuelType = FuelType.from(fuelTypeValue);
        if (carModelRepository.existsSameCarModel(brand, name, generation, fuelType)) {
            throw new IllegalArgumentException("이미 존재하는 차량 모델입니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<CarModelResponse> getCarModels() {
        List<CarModel> carModels = carModelRepository.findAll();
        return carModels.stream()
                .map(CarModelResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public CarModelResponse getCarModel(Long carModelId) {
        CarModel carModel = findModelById(carModelId);
        return CarModelResponse.from(carModel);
    }

    private CarModel findModelById(Long carModelId) {
        return carModelRepository.findById(carModelId)
                .orElseThrow(() -> new IllegalArgumentException("차량 모델이 존재하지 않습니다."));
    }

    @Transactional
    public void deleteCarModel(Long carModelId) {
        CarModel carModel = findModelById(carModelId);
        carModelRepository.delete(carModel);
    }
}
