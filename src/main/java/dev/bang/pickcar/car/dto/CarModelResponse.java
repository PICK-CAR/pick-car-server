package dev.bang.pickcar.car.dto;

import dev.bang.pickcar.car.entity.CarModel;

public record CarModelResponse(
        Long id,
        String brand,
        String name,
        String generation,
        String segment,
        String carType,
        String fuelType,
        int seatCapacity
) {
    public static CarModelResponse from(CarModel carModel) {
        return new CarModelResponse(
                carModel.getId(),
                carModel.getBrand(),
                carModel.getName(),
                carModel.getGeneration(),
                carModel.getSegment().name(),
                carModel.getCarType().name(),
                carModel.getFuelType().name(),
                carModel.getSeatCapacity()
        );
    }
}
