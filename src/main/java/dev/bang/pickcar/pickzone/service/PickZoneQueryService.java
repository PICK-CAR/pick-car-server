package dev.bang.pickcar.pickzone.service;

import dev.bang.pickcar.pickzone.dto.MapBounds;
import dev.bang.pickcar.pickzone.dto.PickZoneDetailResponse;
import dev.bang.pickcar.pickzone.dto.PickZoneMarker;
import dev.bang.pickcar.pickzone.dto.PickZoneResponse;
import dev.bang.pickcar.pickzone.dto.PickZoneResponses;
import dev.bang.pickcar.pickzone.dto.PickZoneSearchCondition;
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
public class PickZoneQueryService {

    private final PickZoneRepository pickZoneRepository;

    @Transactional(readOnly = true)
    public List<PickZoneMarker> getPickZonesNearby(MapBounds mapBounds) {
        return pickZoneRepository.findPickZonesNearby(
                mapBounds.south(),
                mapBounds.north(),
                mapBounds.west(),
                mapBounds.east()
        );
    }

    @Transactional(readOnly = true)
    public PickZoneResponse getPickZone(Long pickZoneId) {
        PickZone pickZone = findPickZoneById(pickZoneId);
        return PickZoneResponse.of(pickZone);
    }

    private PickZone findPickZoneById(Long pickZoneId) {
        return pickZoneRepository.findById(pickZoneId)
                .orElseThrow(() -> new IllegalArgumentException("픽존이 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public PickZoneDetailResponse getPickZoneCars(Long pickZoneId) {
        PickZone pickZone = findPickZoneWithCarsById(pickZoneId);
        return PickZoneDetailResponse.of(pickZone, pickZone.getCars());
    }

    private PickZone findPickZoneWithCarsById(Long pickZoneId) {
        return pickZoneRepository.findPickZoneWithCarsById(pickZoneId)
                .orElseThrow(() -> new IllegalArgumentException("픽존이 존재하지 않습니다."));
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
                PageRequest.of(searchCondition.page(), searchCondition.size())
        );
    }

    private List<PickZoneResponse> getContentFromSearchResult(Slice<PickZone> searchResult) {
        return searchResult.getContent()
                .stream()
                .map(PickZoneResponse::of)
                .toList();
    }
}
