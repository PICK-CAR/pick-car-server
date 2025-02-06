package dev.bang.pickcar.pickzone.controller.docs;

import dev.bang.pickcar.pickzone.dto.PickZoneRequest;
import dev.bang.pickcar.pickzone.dto.PickZoneResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "PickZoneManage", description = "픽존 관리 API (관리자)")
public interface PickZoneManageApiDocs {

    @Operation(summary = "픽존 생성", description = "픽존을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "픽존 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<Void> createPickZone(PickZoneRequest pickZoneRequest);

    @Operation(summary = "픽존 수정", description = "픽존을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "픽존 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<PickZoneResponse> updatePickZone(Long pickZoneId, PickZoneRequest pickZoneRequest);

    @Operation(summary = "픽존 삭제", description = "픽존을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "픽존 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<Void> deletePickZone(Long pickZoneId);
}
