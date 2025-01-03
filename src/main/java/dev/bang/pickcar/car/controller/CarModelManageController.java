package dev.bang.pickcar.car.controller;

import static dev.bang.pickcar.car.CarConstant.CAR_MODEL_RESOURCE_LOCATION;

import dev.bang.pickcar.car.controller.docs.CarModelManageApiDocs;
import dev.bang.pickcar.car.dto.CarModelRequest;
import dev.bang.pickcar.car.dto.CarModelResponse;
import dev.bang.pickcar.car.service.CarModelManageService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("models")
public class CarModelManageController implements CarModelManageApiDocs {

    private final CarModelManageService carModelManageService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<Void> createCarModel(@RequestBody @Valid CarModelRequest carModelRequest) {
        long carModelId = carModelManageService.addCarModel(carModelRequest);
        URI resourceUri = URI.create(CAR_MODEL_RESOURCE_LOCATION + carModelId);
        return ResponseEntity.created(resourceUri).build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<List<CarModelResponse>> getCarModels() {
        return ResponseEntity.ok(carModelManageService.getCarModels());
    }

    @GetMapping("{carModelId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<CarModelResponse> getCarModel(@PathVariable Long carModelId) {
        return ResponseEntity.ok(carModelManageService.getCarModel(carModelId));
    }

    @DeleteMapping("{carModelId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<Void> deleteCarModel(@PathVariable Long carModelId) {
        carModelManageService.deleteCarModel(carModelId);
        return ResponseEntity.noContent().build();
    }
}
