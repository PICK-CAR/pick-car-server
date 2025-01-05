package dev.bang.pickcar.pickzone.dto;

import java.util.List;

public record PickZoneResponses(
        List<PickZoneResponse> pickZoneResponses,
        boolean hasNext
) {
}
