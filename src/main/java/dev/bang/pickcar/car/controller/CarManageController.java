package dev.bang.pickcar.car.controller;

import static dev.bang.pickcar.car.CarConstant.CAR_RESOURCE_LOCATION;

import dev.bang.pickcar.car.controller.docs.CarManageApiDocs;
import dev.bang.pickcar.car.dto.CarRequest;
import dev.bang.pickcar.car.service.CarManageService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("cars")
public class CarManageController implements CarManageApiDocs {

    private final CarManageService carManageService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<Void> createCar(@RequestBody @Valid CarRequest carRequest) {
        long carId = carManageService.addCar(carRequest);
        URI resourceUri = URI.create(CAR_RESOURCE_LOCATION + carId);
        return ResponseEntity.created(resourceUri).build();
    }

    @DeleteMapping("{carId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<Void> deleteCar(@PathVariable Long carId) {
        carManageService.deleteCar(carId);
        return ResponseEntity.noContent().build();
    }
}
