package dev.bang.pickcar.pickzone.entity;

import static dev.bang.pickcar.pickzone.PickZoneTestData.VALID_LATITUDE;
import static dev.bang.pickcar.pickzone.PickZoneTestData.VALID_LONGITUDE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("위치 정보 테스트")
class LocationTest {

    @DisplayName("정상적인 위치 정보 생성은 예외가 발생하지 않는다.")
    @Test
    void shouldNotThrowException_WhenValidCreate() {
        assertDoesNotThrow(() -> Location.of(VALID_LATITUDE, VALID_LONGITUDE));
    }

    @DisplayName("위치 정보는 위도와 경도로 구성된다.")
    @Test
    void shouldHaveLatitudeAndLongitude() {
        Location location = Location.of(VALID_LATITUDE, VALID_LONGITUDE);

        assertThat(location.getLatitude()).isEqualTo(VALID_LATITUDE);
        assertThat(location.getLongitude()).isEqualTo(VALID_LONGITUDE);
    }

    @DisplayName("위도 및 경도가 유효하지 않은 경우 예외가 발생한다.")
    @ParameterizedTest
    @CsvSource({"-91, 0", "91, 0", "0, -181", "0, 181"})
    void shouldThrowException_WhenLatitudeOrLongitudeIsNull(double latitude, double longitude) {
        assertThatThrownBy(() -> Location.of(latitude, longitude))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
