package dev.bang.pickcar.car.entity;

import lombok.Getter;

@Getter
public enum CarStatus {

    UNBATCHED("배치되지 않음"),
    AVAILABLE("사용 가능"),
    UNDER_MAINTENANCE("정비 중"),
    UNAVAILABLE("대여 불가"),
    ;

    private final String description;

    CarStatus(String description) {
        this.description = description;
    }
}
