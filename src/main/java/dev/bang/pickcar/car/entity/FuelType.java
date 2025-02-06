package dev.bang.pickcar.car.entity;

import lombok.Getter;

@Getter
public enum FuelType {

    GASOLINE("가솔린"),
    DIESEL("디젤"),
    ELECTRIC("전기"),
    HYBRID("하이브리드"),
    LPG("LPG"),
    HYDROGEN("수소"),
    ;

    private final String description;

    FuelType(String description) {
        this.description = description;
    }

    public static FuelType fromString(String fuelType) {
        try {
            return FuelType.valueOf(fuelType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효한 연료 타입을 입력해주세요. e.g. GASOLINE, DIESEL, ELECTRIC, HYBRID, LPG, HYDROGEN");
        }
    }
}
