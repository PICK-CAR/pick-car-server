package dev.bang.pickcar.car.entity;

import lombok.Getter;

@Getter
public enum CarStatus {

    AVAILABLE("예약 가능"),
    RESERVED("예약 중"),
    IN_USE("사용 중"),
    UNDER_MAINTENANCE("정비 중"),
    UNAVAILABLE("대여 불가"),
    ;

    private final String description;

    CarStatus(String description) {
        this.description = description;
    }
}
