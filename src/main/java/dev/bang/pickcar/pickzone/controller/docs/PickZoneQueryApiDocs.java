package dev.bang.pickcar.pickzone.controller.docs;

import dev.bang.pickcar.pickzone.dto.MapBounds;
import dev.bang.pickcar.pickzone.dto.PickZoneDetailResponse;
import dev.bang.pickcar.pickzone.dto.PickZoneMarker;
import dev.bang.pickcar.pickzone.dto.PickZoneResponse;
import dev.bang.pickcar.pickzone.dto.PickZoneResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "PickZoneQuery", description = "픽존 조회 API")
public interface PickZoneQueryApiDocs {

    @Operation(summary = "내 주변 픽존 위치 목록 조회", description = "특정 좌표 주변 반경 내 픽존 위치 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "픽존 위치 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<List<PickZoneMarker>> getPickZonesNearby(MapBounds mapBounds);

    @Operation(summary = "id로 픽존 조회", description = "id로 픽존을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "픽존 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<PickZoneResponse> getPickZone(Long pickZoneId);

    @Operation(summary = "특정 픽존 내 차량 목록 조회", description = "특정 픽존 내 차량 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "차량 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<PickZoneDetailResponse> getPickZoneCars(Long pickZoneId);

    @Operation(summary = "픽존 목록 조회", description = "모든 픽존을 조회합니다. (이름, 위도, 경도, 페이지, 크기 조건 검색 가능)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "픽존 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<PickZoneResponses> getPickZones(String keyword, Integer page, Integer size);
}
