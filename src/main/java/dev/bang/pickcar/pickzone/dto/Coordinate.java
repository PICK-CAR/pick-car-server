package dev.bang.pickcar.pickzone.dto;

import static dev.bang.pickcar.pickzone.PickZoneConstant.validateCoordinate;

public record Coordinate(
        double latitude,
        double longitude
) {
    public Coordinate {
        validateCoordinate(latitude, longitude);
    }
}
