package dev.bang.pickcar.pickzone.dto;

import dev.bang.pickcar.car.dto.CarResponse;
import dev.bang.pickcar.car.entity.Car;
import dev.bang.pickcar.pickzone.entity.PickZone;
import java.util.List;

public record PickZoneDetailResponse(
        PickZoneMarker pickZone,
        List<CarResponse> cars
) {
    public static PickZoneDetailResponse of(PickZone pickZone, List<Car> cars) {
        return new PickZoneDetailResponse(
                getPickZoneMarker(pickZone),
                getCarResponses(cars)
        );
    }

    private static PickZoneMarker getPickZoneMarker(PickZone pickZone) {
        return new PickZoneMarker(
                pickZone.getId(),
                pickZone.getName(),
                pickZone.getLocation().getLatitude(),
                pickZone.getLocation().getLongitude()
        );
    }

    private static List<CarResponse> getCarResponses(List<Car> cars) {
        return cars.stream()
                .map(CarResponse::of)
                .toList();
    }
}
