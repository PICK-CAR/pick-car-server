package dev.bang.pickcar.pickzone;

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
                PickZoneTestData.VALID_LATITUDE,
                PickZoneTestData.VALID_LONGITUDE
        );
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
}
