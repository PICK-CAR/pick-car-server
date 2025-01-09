package dev.bang.pickcar.pickzone.entity;

import static dev.bang.pickcar.pickzone.PickZoneConstant.validateCoordinate;

import jakarta.persistence.Embeddable;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Location {

    private double latitude;
    private double longitude;

    private Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Location of(double latitude, double longitude) {
        validateCoordinate(latitude, longitude);
        return new Location(latitude, longitude);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Location location)) {
            return false;
        }
        return Double.compare(latitude, location.latitude) == 0
                && Double.compare(longitude, location.longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    @Override
    public String toString() {
        return "Location{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
