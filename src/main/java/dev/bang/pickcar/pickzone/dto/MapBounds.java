package dev.bang.pickcar.pickzone.dto;

import jakarta.validation.constraints.NotNull;

public record MapBounds(
        @NotNull(message = "중심 좌표를 입력해주세요.")
        Coordinate center,
        @NotNull(message = "북동 좌표를 입력해주세요.")
        Coordinate northEast,
        @NotNull(message = "남서 좌표를 입력해주세요.")
        Coordinate southWest
) {
    public double south() {
        return southWest.latitude();
    }

    public double north() {
        return northEast.latitude();
    }

    public double west() {
        return southWest.longitude();
    }

    public double east() {
        return northEast.longitude();
    }
}
