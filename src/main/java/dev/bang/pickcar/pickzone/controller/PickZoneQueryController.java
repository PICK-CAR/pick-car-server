package dev.bang.pickcar.pickzone.controller;

import dev.bang.pickcar.pickzone.controller.docs.PickZoneQueryApiDocs;
import dev.bang.pickcar.pickzone.dto.MapBounds;
import dev.bang.pickcar.pickzone.dto.PickZoneDetailResponse;
import dev.bang.pickcar.pickzone.dto.PickZoneMarker;
import dev.bang.pickcar.pickzone.dto.PickZoneResponse;
import dev.bang.pickcar.pickzone.dto.PickZoneResponses;
import dev.bang.pickcar.pickzone.dto.PickZoneSearchCondition;
import dev.bang.pickcar.pickzone.service.PickZoneQueryService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("pick-zones")
public class PickZoneQueryController implements PickZoneQueryApiDocs {

    private final PickZoneQueryService pickZoneQueryService;

    @PostMapping("nearby")
    @PreAuthorize("hasRole('MEMBER')")
    @Override
    public ResponseEntity<List<PickZoneMarker>> getPickZonesNearby(@RequestBody @Valid MapBounds mapBounds) {
        return ResponseEntity.ok(pickZoneQueryService.getPickZonesNearby(mapBounds));
    }

    @GetMapping("{pickZoneId}")
    @Override
    public ResponseEntity<PickZoneResponse> getPickZone(@PathVariable("pickZoneId") Long pickZoneId) {
        return ResponseEntity.ok(pickZoneQueryService.getPickZone(pickZoneId));
    }

    @GetMapping("{pickZoneId}/cars")
    @Override
    public ResponseEntity<PickZoneDetailResponse> getPickZoneCars(@PathVariable("pickZoneId") Long pickZoneId) {
        return ResponseEntity.ok(pickZoneQueryService.getPickZoneCars(pickZoneId));
    }

    @GetMapping
    @Override
    public ResponseEntity<PickZoneResponses> getPickZones(@RequestParam(required = false) String keyword,
                                                          @RequestParam(required = false) Integer page,
                                                          @RequestParam(required = false) Integer size) {
        PickZoneSearchCondition searchCondition = new PickZoneSearchCondition(keyword, page, size);
        return ResponseEntity.ok(pickZoneQueryService.getPickZones(searchCondition));
    }
}
