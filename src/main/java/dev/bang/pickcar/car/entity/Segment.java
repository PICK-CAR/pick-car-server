package dev.bang.pickcar.car.entity;

import lombok.Getter;

@Getter
public enum Segment {

    A("경형", "A"),
    B("소형", "B"),
    C("준중형", "C"),
    D("중형", "D"),
    E("준대형", "E"),
    F("대형", "F"),
    ;

    private final String description;
    private final String code; // 유럽 자동차 분류 코드 e.g. A-segment

    Segment(String description, String code) {
        this.description = description;
        this.code = code;
    }

    public static Segment from(String code) {
        try {
            return Segment.valueOf(code.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효한 세그먼트 코드를 입력해주세요. e.g. A, B, C, D, E, F");
        }
    }
}
