package dev.bang.pickcar.pickzone.controller;

import static dev.bang.pickcar.pickzone.PickZoneConstant.PICK_ZONE_RESOURCE_LOCATION;

import dev.bang.pickcar.pickzone.dto.PickZoneRequest;
import dev.bang.pickcar.pickzone.dto.PickZoneResponse;
import dev.bang.pickcar.pickzone.service.PickZoneManageService;
import dev.bang.pickcar.pickzone.controller.docs.PickZoneManageApiDocs;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("pick-zones")
public class PickZoneManageController implements PickZoneManageApiDocs {

    private final PickZoneManageService pickZoneManageService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<Void> createPickZone(@RequestBody @Valid PickZoneRequest pickZoneRequest) {
        long pickZoneId = pickZoneManageService.addPickZone(pickZoneRequest);
        URI resourceUri = URI.create(PICK_ZONE_RESOURCE_LOCATION + pickZoneId);
        return ResponseEntity.created(resourceUri).build();
    }

    @PutMapping("{pickZoneId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<PickZoneResponse> updatePickZone(@PathVariable Long pickZoneId,
                                                           @RequestBody @Valid PickZoneRequest pickZoneRequest) {
        return ResponseEntity.ok(pickZoneManageService.updatePickZone(pickZoneId, pickZoneRequest));
    }

    @DeleteMapping("{pickZoneId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<Void> deletePickZone(@PathVariable Long pickZoneId) {
        pickZoneManageService.deletePickZone(pickZoneId);
        return ResponseEntity.noContent().build();
    }
}
