package dev.bang.pickcar.car.entity;

import static dev.bang.pickcar.car.CarConstant.MIN_CAR_FUEL_LEVEL;
import static dev.bang.pickcar.car.CarConstant.MIN_CAR_HOUR_RATE;
import static dev.bang.pickcar.car.CarConstant.MIN_CAR_MILEAGE;
import static dev.bang.pickcar.car.CarConstant.VIN_LENGTH;

import dev.bang.pickcar.global.entitiy.BaseTimeEntity;
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

    private int hourlyRate;

    private boolean isDeleted = Boolean.FALSE;

    @Builder
    public Car(CarModel model,
               String color,
               String vin,
               String licensePlate,
               LocalDate registrationDate,
               int mileage,
               float fuelLevel,
               Integer hourlyRate) {
        validate(model, color, vin, licensePlate, registrationDate, mileage, fuelLevel);
        this.model = model;
        this.color = color;
        this.vin = vin;
        this.licensePlate = licensePlate;
        this.registrationDate = registrationDate;
        this.mileage = mileage;
        this.fuelLevel = fuelLevel;
        this.hourlyRate = getHourlyRate(hourlyRate, model.getDefaultHourlyRate());
        this.status = CarStatus.UNBATCHED;
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

    private int getHourlyRate(Integer hourlyRate, int defaultHourlyRate) {
        return hourlyRate == null
                ? defaultHourlyRate
                : hourlyRate;
    }

    public void assignPickZone(PickZone pickZone) {
        Assert.notNull(pickZone, "픽존을 입력해주세요.");
        Assert.isTrue(pickZone.isDeleted() == Boolean.FALSE, "삭제된 픽존은 차량에 할당할 수 없습니다.");
        this.pickZone = pickZone;
        this.pickZone.getCars().add(this);
        this.status = CarStatus.AVAILABLE;
    }

    public void updateHourlyRate(int hourlyRate) {
        Assert.isTrue(hourlyRate > MIN_CAR_HOUR_RATE, "시간당 요금은 " + MIN_CAR_HOUR_RATE + "보다 커야 합니다.");
        this.hourlyRate = hourlyRate;
    }

    public boolean isAvailable() {
        return status == CarStatus.AVAILABLE;
    }

    public void maintenance() {
        status = CarStatus.UNDER_MAINTENANCE;
    }

    public void completeMaintenance() {
        if (status != CarStatus.UNDER_MAINTENANCE) {
            throw new IllegalArgumentException("정비 중인 차량만 정비 완료할 수 있습니다.");
        }
        status = CarStatus.AVAILABLE;
    }

    public void unavailable() {
        status = CarStatus.UNAVAILABLE;
    }

    public void available() {
        status = CarStatus.AVAILABLE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Car car = (Car) o;
        return vin.equals(car.vin);
    }

    @Override
    public int hashCode() {
        return vin.hashCode();
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", model=" + model +
                ", color='" + color + '\'' +
                ", vin='" + vin + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", registrationDate=" + registrationDate +
                ", mileage=" + mileage +
                ", fuelLevel=" + fuelLevel +
                ", status=" + status +
                ", pickZone=" + pickZone +
                ", hourlyRate=" + hourlyRate +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
