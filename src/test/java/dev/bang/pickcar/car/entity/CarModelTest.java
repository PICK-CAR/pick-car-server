package dev.bang.pickcar.car.entity;

import static dev.bang.pickcar.car.CarTestData.VALID_CAR_BRAND_NAME;
import static dev.bang.pickcar.car.CarTestData.VALID_CAR_DEFAULT_HOUR_RATE;
import static dev.bang.pickcar.car.CarTestData.VALID_CAR_GENERATION;
import static dev.bang.pickcar.car.CarTestData.VALID_CAR_MODEL_NAME;
import static dev.bang.pickcar.car.CarTestData.VALID_CAR_SEAT_CAPACITY;
import static dev.bang.pickcar.car.CarTestData.VALID_CAR_SEGMENT;
import static dev.bang.pickcar.car.CarTestData.VALID_CAR_TYPE;
import static dev.bang.pickcar.car.CarTestData.VALID_FUEL_TYPE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("차량 모델 테스트")
class CarModelTest {

    @DisplayName("정상적인 차량 모델 생성은 예외가 발생하지 않는다.")
    @Test
    void shouldNotThrowException_WhenValidCreate() {
        assertThatCode(() ->
                new CarModel(
                        VALID_CAR_BRAND_NAME,
                        VALID_CAR_MODEL_NAME,
                        VALID_CAR_GENERATION,
                        VALID_CAR_SEGMENT,
                        VALID_CAR_TYPE,
                        VALID_FUEL_TYPE,
                        VALID_CAR_SEAT_CAPACITY,
                        VALID_CAR_DEFAULT_HOUR_RATE
                )
        ).doesNotThrowAnyException();
    }

    @DisplayName("차량 모델 생성 시 브랜드 이름이 null 또는 빈 문자열인 경우 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowException_WhenBrandNameIsNullOrEmpty(String brandName) {
        assertThatThrownBy(() ->
                new CarModel(
                        brandName,
                        VALID_CAR_MODEL_NAME,
                        VALID_CAR_GENERATION,
                        VALID_CAR_SEGMENT,
                        VALID_CAR_TYPE,
                        VALID_FUEL_TYPE,
                        VALID_CAR_SEAT_CAPACITY,
                        VALID_CAR_DEFAULT_HOUR_RATE
                )
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("차량 모델 생성 시 모델 이름이 null 또는 빈 문자열인 경우 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowException_WhenModelNameIsNullOrEmpty(String modelName) {
        assertThatThrownBy(() ->
                new CarModel(
                        VALID_CAR_BRAND_NAME,
                        modelName,
                        VALID_CAR_GENERATION,
                        VALID_CAR_SEGMENT,
                        VALID_CAR_TYPE,
                        VALID_FUEL_TYPE,
                        VALID_CAR_SEAT_CAPACITY,
                        VALID_CAR_DEFAULT_HOUR_RATE
                )
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("차량 모델 생성 시 세대가 null 또는 빈 문자열인 경우 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowException_WhenGenerationIsNullOrEmpty(String generation) {
        assertThatThrownBy(() ->
                new CarModel(
                        VALID_CAR_BRAND_NAME,
                        VALID_CAR_MODEL_NAME,
                        generation,
                        VALID_CAR_SEGMENT,
                        VALID_CAR_TYPE,
                        VALID_FUEL_TYPE,
                        VALID_CAR_SEAT_CAPACITY,
                        VALID_CAR_DEFAULT_HOUR_RATE
                )
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("차량 모델 생성 시 세그먼트가 null인 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenSegmentIsNull() {
        assertThatThrownBy(() ->
                new CarModel(
                        VALID_CAR_BRAND_NAME,
                        VALID_CAR_MODEL_NAME,
                        VALID_CAR_GENERATION,
                        null,
                        VALID_CAR_TYPE,
                        VALID_FUEL_TYPE,
                        VALID_CAR_SEAT_CAPACITY,
                        VALID_CAR_DEFAULT_HOUR_RATE
                )
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("차량 모델 생성 시 차량 종류가 null인 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenCarTypeIsNull() {
        assertThatThrownBy(() ->
                new CarModel(
                        VALID_CAR_BRAND_NAME,
                        VALID_CAR_MODEL_NAME,
                        VALID_CAR_GENERATION,
                        VALID_CAR_SEGMENT,
                        null,
                        VALID_FUEL_TYPE,
                        VALID_CAR_SEAT_CAPACITY,
                        VALID_CAR_DEFAULT_HOUR_RATE
                )
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("차량 모델 생성 시 연료 타입이 null인 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenFuelTypeIsNull() {
        assertThatThrownBy(() ->
                new CarModel(
                        VALID_CAR_BRAND_NAME,
                        VALID_CAR_MODEL_NAME,
                        VALID_CAR_GENERATION,
                        VALID_CAR_SEGMENT,
                        VALID_CAR_TYPE,
                        null,
                        VALID_CAR_SEAT_CAPACITY,
                        VALID_CAR_DEFAULT_HOUR_RATE
                )
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("차량 모델 생성 시 좌석 수가 0 이하인 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void shouldThrowException_WhenSeatCapacityIsNotValid(int seatCapacity) {
        assertThatThrownBy(() ->
                new CarModel(
                        VALID_CAR_BRAND_NAME,
                        VALID_CAR_MODEL_NAME,
                        VALID_CAR_GENERATION,
                        VALID_CAR_SEGMENT,
                        VALID_CAR_TYPE,
                        VALID_FUEL_TYPE,
                        seatCapacity,
                        VALID_CAR_DEFAULT_HOUR_RATE
                )
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("차량 모델 생성 시 시간당 요금이 0 이하인 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void shouldUseDefaultHourlyRate_WhenHourlyRateIsNotValid(int hourlyRate) {
        assertThatThrownBy(() ->
                new CarModel(
                        VALID_CAR_BRAND_NAME,
                        VALID_CAR_MODEL_NAME,
                        VALID_CAR_GENERATION,
                        VALID_CAR_SEGMENT,
                        VALID_CAR_TYPE,
                        VALID_FUEL_TYPE,
                        VALID_CAR_SEAT_CAPACITY,
                        hourlyRate
                )
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
