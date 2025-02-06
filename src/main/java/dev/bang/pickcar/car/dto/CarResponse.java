package dev.bang.pickcar.car.dto;

import dev.bang.pickcar.car.entity.Car;

public record CarResponse(
        long id,
        String brand,
        String name,
        String generation,
        String segment,
        String carType,
        String fuelType,
        int seatCapacity,
        String color,
        String licensePlate,
        float fuelLevel,
        int hourlyRate,
        String status
) {
    public static CarResponse of(Car car) {
        return new CarResponse(
                car.getId(),
                car.getModel().getBrand(),
                car.getModel().getName(),
                car.getModel().getGeneration(),
                car.getModel().getSegment().name(),
                car.getModel().getCarType().name(),
                car.getModel().getFuelType().name(),
                car.getModel().getSeatCapacity(),
                car.getColor(),
                car.getLicensePlate(),
                car.getFuelLevel(),
                car.getHourlyRate(),
                car.getStatus().name()
        );
    }
}
