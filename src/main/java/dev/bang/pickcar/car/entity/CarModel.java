package dev.bang.pickcar.car.entity;

import static dev.bang.pickcar.car.CarConstant.MIN_SEAT_CAPACITY;

import dev.bang.pickcar.entitiy.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.util.Assert;

@Table(name = "car_models")
@Entity
@SQLDelete(sql = "UPDATE car_models SET is_deleted = true WHERE car_model_id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CarModel extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "car_model_id")
    private Long id;

    private String brand;

    private String name;

    private String generation;

    @Enumerated(EnumType.STRING)
    private Segment segment;

    @Enumerated(EnumType.STRING)
    private CarType carType;

    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    private int seatCapacity;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = Boolean.FALSE;

    public CarModel(String brand,
                    String name,
                    String generation,
                    Segment segment,
                    CarType carType,
                    FuelType fuelType,
                    int seatCapacity) {
        validate(brand, name, generation, segment, carType, fuelType, seatCapacity);
        this.brand = brand;
        this.name = name;
        this.generation = generation;
        this.segment = segment;
        this.carType = carType;
        this.fuelType = fuelType;
        this.seatCapacity = seatCapacity;
    }

    private void validate(String brand,
                          String name,
                          String generation,
                          Segment segment,
                          CarType carType,
                          FuelType fuelType,
                          int seatCapacity) {
        Assert.hasText(brand, "브랜드를 입력해주세요.");
        Assert.hasText(name, "모델명을 입력해주세요.");
        Assert.hasText(generation, "세대를 입력해주세요.");
        Assert.notNull(segment, "세그먼트를 입력해주세요.");
        Assert.notNull(carType, "차량 종류를 입력해주세요.");
        Assert.notNull(fuelType, "연료 타입을 입력해주세요.");
        Assert.isTrue(seatCapacity > MIN_SEAT_CAPACITY, "좌석 수는 " + MIN_SEAT_CAPACITY + "보다 많아야 합니다.");
    }
}
