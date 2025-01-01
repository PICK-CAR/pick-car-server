package dev.bang.pickcar.car.dto;

import dev.bang.pickcar.car.entity.CarModel;
import dev.bang.pickcar.car.entity.CarType;
import dev.bang.pickcar.car.entity.FuelType;
import dev.bang.pickcar.car.entity.Segment;
import jakarta.validation.constraints.NotBlank;

public record CarModelRequest(
        @NotBlank(message = "브랜드를 입력해주세요.")
        String brand,
        @NotBlank(message = "모델명을 입력해주세요.")
        String name,
        @NotBlank(message = "세대를 입력해주세요.")
        String generation,
        @NotBlank(message = "세그먼트를 입력해주세요. e.g. A, B")
        String segment,
        @NotBlank(message = "차량 종류를 입력해주세요. e.g. SEDAN, SUV")
        String carType,
        @NotBlank(message = "연료 타입을 입력해주세요. e.g. GASOLINE, DIESEL")
        String fuelType,
        int seatCapacity
) {
    public CarModel toCarModel() {
        return new CarModel(
                brand,
                name,
                generation,
                Segment.from(segment),
                CarType.from(carType),
                FuelType.from(fuelType),
                seatCapacity
        );
    }
}
