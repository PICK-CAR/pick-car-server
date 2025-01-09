package dev.bang.pickcar.pickzone.dto;

public record PickZoneMarker(
        long id,
        String name,
        Coordinate coordinate
) {
    public PickZoneMarker(long id, String name, double latitude, double longitude) {
        this(id, name, new Coordinate(latitude, longitude));
    }
}
