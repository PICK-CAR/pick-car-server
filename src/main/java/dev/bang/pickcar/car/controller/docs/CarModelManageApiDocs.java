package dev.bang.pickcar.car.controller.docs;

import dev.bang.pickcar.car.dto.CarModelRequest;
import dev.bang.pickcar.car.dto.CarModelResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "CarModelManage", description = "차량 모델 관리 API (관리자)")
public interface CarModelManageApiDocs {

    @Operation(summary = "차량 모델 생성", description = "차량 모델을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "차량 모델 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<Void> createCarModel(CarModelRequest carModelRequest);

    @Operation(summary = "차량 모델 목록 조회", description = "모든 차량 모델을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "차량 모델 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<List<CarModelResponse>> getCarModels();

    @Operation(summary = "차량 모델 조회", description = "차량 모델을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "차량 모델 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<CarModelResponse> getCarModel(Long carModelId);

    @Operation(summary = "차량 모델 삭제", description = "차량 모델을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "차량 모델 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<Void> deleteCarModel(Long carModelId);
}
