package dev.bang.pickcar.car.entity;

import static dev.bang.pickcar.car.CarConstant.MIN_CAR_FUEL_LEVEL;
import static dev.bang.pickcar.car.CarConstant.MIN_CAR_MILEAGE;
import static dev.bang.pickcar.car.CarConstant.VIN_LENGTH;

import dev.bang.pickcar.entitiy.BaseTimeEntity;
import dev.bang.pickcar.pickzone.entity.PickZone;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.util.Assert;

@Table(name = "cars")
@Entity
@SQLDelete(sql = "UPDATE cars SET is_deleted = true WHERE car_id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Car extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "car_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_model_id")
    private CarModel model;

    private String color;

    @Column(name = "vin", unique = true)
    private String vin;

    @Column(name = "license_plate", unique = true)
    private String licensePlate;

    private LocalDate registrationDate;

    private int mileage;

    private float fuelLevel;

    @Enumerated(EnumType.STRING)
    private CarStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pick_zone_id")
    private PickZone pickZone;

    private boolean isDeleted = Boolean.FALSE;

    @Builder
    public Car(CarModel model,
               String color,
               String vin,
               String licensePlate,
               LocalDate registrationDate,
               int mileage,
               float fuelLevel) {
        validate(model, color, vin, licensePlate, registrationDate, mileage, fuelLevel);
        this.model = model;
        this.color = color;
        this.vin = vin;
        this.licensePlate = licensePlate;
        this.registrationDate = registrationDate;
        this.mileage = mileage;
        this.fuelLevel = fuelLevel;
        this.status = CarStatus.AVAILABLE;
    }

    private void validate(CarModel model,
                          String color,
                          String vin,
                          String licensePlate,
                          LocalDate registrationDate,
                          int mileage,
                          float fuelLevel) {
        Assert.notNull(model, "차량 모델을 입력해주세요.");
        Assert.hasText(color, "색상을 입력해주세요.");
        Assert.hasText(vin, "차대번호를 입력해주세요.");
        Assert.isTrue(vin.length() == VIN_LENGTH, "차대번호는 " + VIN_LENGTH + "자여야 합니다.");
        Assert.hasText(licensePlate, "차량 번호를 입력해주세요.");
        Assert.notNull(registrationDate, "등록일을 입력해주세요.");
        Assert.isTrue(mileage >= MIN_CAR_MILEAGE, "주행거리는 " + MIN_CAR_MILEAGE + " 이상이어야 합니다.");
        Assert.isTrue(fuelLevel >= MIN_CAR_FUEL_LEVEL, "연료량은 " + MIN_CAR_FUEL_LEVEL + " 이상이어야 합니다.");
    }

    public void assignPickZone(PickZone pickZone) {
        Assert.notNull(pickZone, "픽존을 입력해주세요.");
        Assert.isTrue(pickZone.isDeleted() == Boolean.FALSE, "삭제된 픽존은 차량에 할당할 수 없습니다.");
        this.pickZone = pickZone;
    }
}
