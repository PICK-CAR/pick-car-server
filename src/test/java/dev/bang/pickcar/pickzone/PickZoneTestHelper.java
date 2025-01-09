package dev.bang.pickcar.pickzone;

import static dev.bang.pickcar.pickzone.PickZoneTestData.VALID_LATITUDE;
import static dev.bang.pickcar.pickzone.PickZoneTestData.VALID_LONGITUDE;

import dev.bang.pickcar.pickzone.dto.Coordinate;
import dev.bang.pickcar.pickzone.dto.MapBounds;
import dev.bang.pickcar.pickzone.dto.PickZoneRequest;
import dev.bang.pickcar.pickzone.entity.Location;
import dev.bang.pickcar.pickzone.entity.PickZone;
import dev.bang.pickcar.pickzone.repository.PickZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@Component
@ActiveProfiles("test")
public class PickZoneTestHelper {

    @Autowired
    private PickZoneRepository pickZoneRepository;

    public PickZoneRequest createPickZoneRequest() {
        return new PickZoneRequest(
                PickZoneTestData.VALID_NAME,
                PickZoneTestData.VALID_ADDRESS,
                PickZoneTestData.VALID_DETAIL_ADDRESS,
                PickZoneTestData.VALID_DESCRIPTION,
                VALID_LATITUDE,
                PickZoneTestData.VALID_LONGITUDE
        );
    }

    public long createPickZone() {
        return createPickZone(createPickZoneRequest());
    }

    public long createPickZone(PickZoneRequest pickZoneRequest) {
        return pickZoneRepository.save(PickZone.builder()
                .name(pickZoneRequest.name())
                .address(pickZoneRequest.address())
                .detailAddress(pickZoneRequest.detailAddress())
                .description(pickZoneRequest.description())
                .location(Location.of(pickZoneRequest.latitude(), pickZoneRequest.longitude()))
                .build()).getId();
    }

    public MapBounds createMapBounds() {
        return new MapBounds(
                new Coordinate(VALID_LATITUDE, VALID_LONGITUDE),
                new Coordinate(VALID_LATITUDE + 0.001, VALID_LONGITUDE + 0.001),
                new Coordinate(VALID_LATITUDE - 0.001, VALID_LONGITUDE - 0.001)
        );
    }
}
