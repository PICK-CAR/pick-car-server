package dev.bang.pickcar.pickzone.dto;

import dev.bang.pickcar.pickzone.entity.PickZone;

public record PickZoneResponse(
        Long id,
        String name,
        String address,
        String detailAddress,
        String description,
        double latitude,
        double longitude
) {
    public static PickZoneResponse of(PickZone pickZone) {
        return new PickZoneResponse(
                pickZone.getId(),
                pickZone.getName(),
                pickZone.getAddress(),
                pickZone.getDetailAddress(),
                pickZone.getDescription(),
                pickZone.getLocation().getLatitude(),
                pickZone.getLocation().getLongitude()
        );
    }
}
