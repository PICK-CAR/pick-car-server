package dev.bang.pickcar.car.entity;

import lombok.Getter;

@Getter
public enum CarType {

    SEDAN("세단"),
    SUV("SUV"),
    HATCHBACK("해치백"),
    TRUCK("트럭"),
    VAN("밴"),
    COUPE("쿠페"),
    CONVERTIBLE("컨버터블"),
    ;

    private final String description;

    CarType(String description) {
        this.description = description;
    }

    public static CarType from(String carType) {
        try {
            return CarType.valueOf(carType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효한 차량 종류를 입력해주세요. e.g. SEDAN, SUV, HATCHBACK, TRUCK, VAN, COUPE, CONVERTIBLE");
        }
    }
}
