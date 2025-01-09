package dev.bang.pickcar.pickzone.entity;

import dev.bang.pickcar.car.entity.Car;
import dev.bang.pickcar.entitiy.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.util.Assert;

@Table(name = "pick_zones")
@Entity
@SQLDelete(sql = "UPDATE pick_zones SET is_deleted = true WHERE pick_zone_id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PickZone extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pick_zone_id")
    private Long id;

    private String name;

    private String address;

    private String detailAddress;

    private String description;

    @Embedded
    private Location location;

    @OneToMany(mappedBy = "pickZone", fetch = FetchType.LAZY)
    private List<Car> cars = new ArrayList<>();

    private boolean isDeleted = Boolean.FALSE;

    @Builder
    public PickZone(String name,
                    String address,
                    String detailAddress,
                    String description,
                    Location location) {
        validate(name, address, detailAddress, description, location);
        this.name = name;
        this.address = address;
        this.detailAddress = detailAddress;
        this.description = description;
        this.location = location;
    }

    private void validate(String name, String address, String detailAddress, String description, Location location) {
        Assert.hasText(name, "이름은 필수입니다.");
        Assert.hasText(address, "주소는 필수입니다.");
        Assert.hasText(detailAddress, "상세 주소는 필수입니다.");
        Assert.hasText(description, "설명은 필수입니다.");
        Assert.notNull(location, "위치는 필수입니다.");
    }

    public void update(String name, String address, String detailAddress, String description, Location location) {
        updateField(this.name, name, () -> this.name = name, "이름은 필수입니다.");
        updateField(this.address, address, () -> this.address = address, "주소는 필수입니다.");
        updateField(this.detailAddress, detailAddress, () -> this.detailAddress = detailAddress, "상세 주소는 필수입니다.");
        updateField(this.description, description, () -> this.description = description, "설명은 필수입니다.");
        updateField(this.location, location, () -> this.location = location, "위치는 필수입니다.");
    }

    private <T> void updateField(T currentValue, T newValue, Runnable updateAction, String errorMessage) {
        Assert.notNull(newValue, errorMessage);
        if (newValue instanceof String) {
            Assert.hasText((String) newValue, errorMessage);
        }
        if (!currentValue.equals(newValue)) {
            updateAction.run();
        }
    }
}
