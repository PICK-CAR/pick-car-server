package dev.bang.pickcar.pickzone;

public class PickZoneConstant {

    public static final String PICK_ZONE_RESOURCE_LOCATION = "/pick-zones/";

    public static final double MIN_LATITUDE = -90.0;
    public static final double MAX_LATITUDE = 90.0;
    public static final double MIN_LONGITUDE = -180.0;
    public static final double MAX_LONGITUDE = 180.0;

    public static final int MIN_PICK_ZONE_SEARCH_PAGE = 0;
    public static final int MIN_PICK_ZONE_SEARCH_SIZE = 10;

    public static void validateCoordinate(double latitude, double longitude) {
        if (latitude < MIN_LATITUDE || latitude > MAX_LATITUDE) {
            throw new IllegalArgumentException("위도는 " + MIN_LATITUDE + "에서 " + MAX_LATITUDE + "사이여야 합니다.");
        }
        if (longitude < MIN_LONGITUDE || longitude > MAX_LONGITUDE) {
            throw new IllegalArgumentException("경도는 " + MIN_LONGITUDE + "에서 " + MAX_LONGITUDE + "사이여야 합니다.");
        }
    }
}
