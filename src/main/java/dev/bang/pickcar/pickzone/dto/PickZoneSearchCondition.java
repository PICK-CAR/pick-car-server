package dev.bang.pickcar.pickzone.dto;

import static dev.bang.pickcar.pickzone.PickZoneConstant.MIN_PICK_ZONE_SEARCH_PAGE;
import static dev.bang.pickcar.pickzone.PickZoneConstant.MIN_PICK_ZONE_SEARCH_SIZE;

public record PickZoneSearchCondition(
        String keyword,
        Integer page,
        Integer size
) {
    public PickZoneSearchCondition {
        if (page == null || page < 0) {
            page = MIN_PICK_ZONE_SEARCH_PAGE;
        }
        if (size == null || size < 1) {
            size = MIN_PICK_ZONE_SEARCH_SIZE;
        }
    }
}
