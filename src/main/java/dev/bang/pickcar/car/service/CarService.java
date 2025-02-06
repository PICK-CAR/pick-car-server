package dev.bang.pickcar.car.service;

import dev.bang.pickcar.car.entity.Car;
import dev.bang.pickcar.car.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    @Transactional(readOnly = true)
    public Car findById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 차량이 존재하지 않습니다."));
    }
}
