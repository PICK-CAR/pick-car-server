package dev.bang.pickcar.car.entity;

import static dev.bang.pickcar.car.CarTestData.VALID_CAR_COLOR;
import static dev.bang.pickcar.car.CarTestData.VALID_CAR_FUEL_LEVEL;
import static dev.bang.pickcar.car.CarTestData.VALID_CAR_HOUR_RATE;
import static dev.bang.pickcar.car.CarTestData.VALID_CAR_MILEAGE;
import static dev.bang.pickcar.car.CarTestData.VALID_CAR_MODEL;
import static dev.bang.pickcar.car.CarTestData.VALID_CAR_NUMBER;
import static dev.bang.pickcar.car.CarTestData.VALID_CAR_REGISTRATION_DATE;
import static dev.bang.pickcar.car.CarTestData.VALID_CAR_VIN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("공유 차량 테스트")
class CarTest {

    @DisplayName("정상적인 공유 차량 생성은 예외가 발생하지 않는다.")
    @Test
    void shouldNotThrowException_WhenValidCreate() {
        assertThatCode(() -> Car.builder()
                .model(VALID_CAR_MODEL)
                .color(VALID_CAR_COLOR)
                .vin(VALID_CAR_VIN)
                .licensePlate(VALID_CAR_NUMBER)
                .registrationDate(VALID_CAR_REGISTRATION_DATE)
                .mileage(VALID_CAR_MILEAGE)
                .fuelLevel(VALID_CAR_FUEL_LEVEL)
                .hourlyRate(VALID_CAR_HOUR_RATE)
                .build()
        ).doesNotThrowAnyException();
    }

    @DisplayName("공유 차량 생성 시 모델이 null인 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenModelIsNull() {
        assertThatThrownBy(() -> Car.builder()
                .model(null)
                .color(VALID_CAR_COLOR)
                .vin(VALID_CAR_VIN)
                .licensePlate(VALID_CAR_NUMBER)
                .registrationDate(VALID_CAR_REGISTRATION_DATE)
                .mileage(VALID_CAR_MILEAGE)
                .fuelLevel(VALID_CAR_FUEL_LEVEL)
                .hourlyRate(VALID_CAR_HOUR_RATE)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("공유 차량 생성 시 색상이 null 또는 빈 문자열인 경우 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowException_WhenColorIsNullOrEmpty(String color) {
        assertThatThrownBy(() -> Car.builder()
                .model(VALID_CAR_MODEL)
                .color(color)
                .vin(VALID_CAR_VIN)
                .licensePlate(VALID_CAR_NUMBER)
                .registrationDate(VALID_CAR_REGISTRATION_DATE)
                .mileage(VALID_CAR_MILEAGE)
                .fuelLevel(VALID_CAR_FUEL_LEVEL)
                .hourlyRate(VALID_CAR_HOUR_RATE)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("공유 차량 생성 시 차대번호가 null 또는 빈 문자열인 경우 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowException_WhenVinIsNullOrEmpty(String vin) {
        assertThatThrownBy(() -> Car.builder()
                .model(VALID_CAR_MODEL)
                .color(VALID_CAR_COLOR)
                .vin(vin)
                .licensePlate(VALID_CAR_NUMBER)
                .registrationDate(VALID_CAR_REGISTRATION_DATE)
                .mileage(VALID_CAR_MILEAGE)
                .fuelLevel(VALID_CAR_FUEL_LEVEL)
                .hourlyRate(VALID_CAR_HOUR_RATE)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("공유 차량 생성 시 차대번호의 길이가 17자가 아닌 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"1234567890123456, 123456789012345678"})
    void shouldThrowException_WhenVinLengthIsNotSeventeen(String vin) {
        assertThatThrownBy(() -> Car.builder()
                .model(VALID_CAR_MODEL)
                .color(VALID_CAR_COLOR)
                .vin(vin)
                .licensePlate(VALID_CAR_NUMBER)
                .registrationDate(VALID_CAR_REGISTRATION_DATE)
                .mileage(VALID_CAR_MILEAGE)
                .fuelLevel(VALID_CAR_FUEL_LEVEL)
                .hourlyRate(VALID_CAR_HOUR_RATE)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("공유 차량 생성 시 번호판(차량 번호)이 null 또는 빈 문자열인 경우 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowException_WhenLicensePlateIsNullOrEmpty(String licensePlate) {
        assertThatThrownBy(() -> Car.builder()
                .model(VALID_CAR_MODEL)
                .color(VALID_CAR_COLOR)
                .vin(VALID_CAR_VIN)
                .licensePlate(licensePlate)
                .registrationDate(VALID_CAR_REGISTRATION_DATE)
                .mileage(VALID_CAR_MILEAGE)
                .fuelLevel(VALID_CAR_FUEL_LEVEL)
                .hourlyRate(VALID_CAR_HOUR_RATE)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("공유 차량 생성 시 등록일이 null인 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenRegistrationDateIsNull() {
        assertThatThrownBy(() -> Car.builder()
                .model(VALID_CAR_MODEL)
                .color(VALID_CAR_COLOR)
                .vin(VALID_CAR_VIN)
                .licensePlate(VALID_CAR_NUMBER)
                .registrationDate(null)
                .mileage(VALID_CAR_MILEAGE)
                .fuelLevel(VALID_CAR_FUEL_LEVEL)
                .hourlyRate(VALID_CAR_HOUR_RATE)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("공유 차량 생성 시 주행거리가 0 미만인 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -100})
    void shouldThrowException_WhenMileageIsNegative(int mileage) {
        assertThatThrownBy(() -> Car.builder()
                .model(VALID_CAR_MODEL)
                .color(VALID_CAR_COLOR)
                .vin(VALID_CAR_VIN)
                .licensePlate(VALID_CAR_NUMBER)
                .registrationDate(VALID_CAR_REGISTRATION_DATE)
                .mileage(mileage)
                .fuelLevel(VALID_CAR_FUEL_LEVEL)
                .hourlyRate(VALID_CAR_HOUR_RATE)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("공유 차량 생성 시 연료량이 0 미만인 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(floats = {-1.0f, -100.0f})
    void shouldThrowException_WhenFuelLevelIsNegative(float fuelLevel) {
        assertThatThrownBy(() -> Car.builder()
                .model(VALID_CAR_MODEL)
                .color(VALID_CAR_COLOR)
                .vin(VALID_CAR_VIN)
                .licensePlate(VALID_CAR_NUMBER)
                .registrationDate(VALID_CAR_REGISTRATION_DATE)
                .mileage(VALID_CAR_MILEAGE)
                .fuelLevel(fuelLevel)
                .hourlyRate(VALID_CAR_HOUR_RATE)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("공유 차량 생성 시 시간당 요금을 지정하지 않은 경우 차량 모델의 기본 시간당 요금이 설정된다.")
    @Test
    void shouldSetDefaultHourRate_WhenHourRateIsNull() {
        Car car = Car.builder()
                .model(VALID_CAR_MODEL)
                .color(VALID_CAR_COLOR)
                .vin(VALID_CAR_VIN)
                .licensePlate(VALID_CAR_NUMBER)
                .registrationDate(VALID_CAR_REGISTRATION_DATE)
                .mileage(VALID_CAR_MILEAGE)
                .fuelLevel(VALID_CAR_FUEL_LEVEL)
                .hourlyRate(null)
                .build();

        assertThat(car.getHourlyRate()).isEqualTo(VALID_CAR_MODEL.getDefaultHourlyRate());
    }
}
