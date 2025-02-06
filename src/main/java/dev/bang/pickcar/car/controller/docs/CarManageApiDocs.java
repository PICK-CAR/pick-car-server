package dev.bang.pickcar.car.controller.docs;

import dev.bang.pickcar.car.dto.CarHourlyRateRequest;
import dev.bang.pickcar.car.dto.CarRequest;
import dev.bang.pickcar.car.dto.PickZoneAssignRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "CarManage", description = "차량 관리 API (관리자)")
public interface CarManageApiDocs {

    @Operation(summary = "공유 차량 생성", description = "공유 차량을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "공유 차량 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<Void> createCar(CarRequest carRequest);

    @Operation(summary = "공유 차량 PickZone에 배치", description = "공유 차량을 PickZone에 배치합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공유 차량 PickZone 배치 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<Void> assignCarToPickZone(Long carId, PickZoneAssignRequest pickZoneAssignRequest);

    @Operation(summary = "공유 차량 시간당 요금 수정", description = "공유 차량의 시간당 요금을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공유 차량 시간당 요금 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<Void> updateHourlyRate(Long carId, CarHourlyRateRequest hourlyRate);

    @Operation(summary = "공유 차량 삭제", description = "공유 차량을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "공유 차량 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<Void> deleteCar(Long carId);
}
