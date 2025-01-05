package dev.bang.pickcar.pickzone.service;

import dev.bang.pickcar.pickzone.dto.PickZoneRequest;
import dev.bang.pickcar.pickzone.dto.PickZoneResponse;
import dev.bang.pickcar.pickzone.dto.PickZoneResponses;
import dev.bang.pickcar.pickzone.dto.PickZoneSearchCondition;
import dev.bang.pickcar.pickzone.entity.Location;
import dev.bang.pickcar.pickzone.entity.PickZone;
import dev.bang.pickcar.pickzone.repository.PickZoneRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PickZoneManageService {

    private final PickZoneRepository pickZoneRepository;

    @Transactional
    public long addPickZone(PickZoneRequest pickZoneRequest) {
        PickZone pickZone = requestToPickZone(pickZoneRequest);
        return pickZoneRepository.save(pickZone).getId();
    }

    private PickZone requestToPickZone(PickZoneRequest pickZoneRequest) {
        Location location = Location.of(pickZoneRequest.latitude(), pickZoneRequest.longitude());
        return PickZone.builder()
                .name(pickZoneRequest.name())
                .address(pickZoneRequest.address())
                .detailAddress(pickZoneRequest.detailAddress())
                .description(pickZoneRequest.description())
                .location(location)
                .build();
    }

    @Transactional
    public void deletePickZone(Long pickZoneId) {
        PickZone pickZone = findPickZoneById(pickZoneId);
        pickZoneRepository.delete(pickZone);
    }

    private PickZone findPickZoneById(Long pickZoneId) {
        return pickZoneRepository.findById(pickZoneId)
                .orElseThrow(() -> new IllegalArgumentException("픽존이 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public PickZoneResponse getPickZone(Long pickZoneId) {
        PickZone pickZone = findPickZoneById(pickZoneId);
        return PickZoneResponse.of(pickZone);
    }

    @Transactional(readOnly = true)
    public PickZoneResponses getPickZones(PickZoneSearchCondition searchCondition) {
        Slice<PickZone> searchResult = getSearchResult(searchCondition);
        List<PickZoneResponse> content = getContentFromSearchResult(searchResult);
        return new PickZoneResponses(content, searchResult.hasNext());
    }

    private Slice<PickZone> getSearchResult(PickZoneSearchCondition searchCondition) {
        return pickZoneRepository.searchPickZones(
                searchCondition.keyword(),
                getLocationIfPresent(searchCondition.latitude(), searchCondition.longitude()),
                PageRequest.of(searchCondition.page(), searchCondition.size())
        );
    }

    private Location getLocationIfPresent(Double latitude, Double longitude) {
        if (latitude == null || longitude == null) {
            return null;
        }
        return Location.of(latitude, longitude);
    }

    private List<PickZoneResponse> getContentFromSearchResult(Slice<PickZone> searchResult) {
        return searchResult.getContent()
                .stream()
                .map(PickZoneResponse::of)
                .toList();
    }

    @Transactional
    public PickZoneResponse updatePickZone(Long pickZoneId, PickZoneRequest pickZoneRequest) {
        PickZone pickZone = findPickZoneById(pickZoneId);
        update(pickZone, pickZoneRequest);
        return PickZoneResponse.of(pickZone);
    }

    private void update(PickZone pickZone, PickZoneRequest pickZoneRequest) {
        Location location = Location.of(pickZoneRequest.latitude(), pickZoneRequest.longitude());
        pickZone.update(
                pickZoneRequest.name(),
                pickZoneRequest.address(),
                pickZoneRequest.detailAddress(),
                pickZoneRequest.description(),
                location
        );
    }
}
