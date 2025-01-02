package dev.bang.pickcar.car.controller.docs;

import dev.bang.pickcar.car.dto.CarRequest;
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
}
