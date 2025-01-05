package dev.bang.pickcar.pickzone.entity;

import static dev.bang.pickcar.pickzone.PickZoneConstant.MAX_LATITUDE;
import static dev.bang.pickcar.pickzone.PickZoneConstant.MAX_LONGITUDE;
import static dev.bang.pickcar.pickzone.PickZoneConstant.MIN_LATITUDE;
import static dev.bang.pickcar.pickzone.PickZoneConstant.MIN_LONGITUDE;

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
        validate(latitude, longitude);
        return new Location(latitude, longitude);
    }

    private static void validate(double latitude, double longitude) {
        if (latitude < MIN_LATITUDE || latitude > MAX_LATITUDE) {
            throw new IllegalArgumentException("올바르지 않은 위도입니다: " + latitude);
        }
        if (longitude < MIN_LONGITUDE || longitude > MAX_LONGITUDE) {
            throw new IllegalArgumentException("올바르지 않은 경도입니다: " + longitude);
        }
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
}
