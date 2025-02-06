package dev.bang.pickcar.pickzone.entity;

import static dev.bang.pickcar.pickzone.PickZoneTestData.VALID_ADDRESS;
import static dev.bang.pickcar.pickzone.PickZoneTestData.VALID_DESCRIPTION;
import static dev.bang.pickcar.pickzone.PickZoneTestData.VALID_DETAIL_ADDRESS;
import static dev.bang.pickcar.pickzone.PickZoneTestData.VALID_LATITUDE;
import static dev.bang.pickcar.pickzone.PickZoneTestData.VALID_NAME;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@DisplayName("픽존 테스트")
class PickZoneTest {

    @DisplayName("정상적인 픽존 생성은 예외가 발생하지 않는다.")
    @Test
    void shouldNotThrowException_WhenValidCreate() {
        assertDoesNotThrow(
                () -> PickZone.builder()
                        .name(VALID_NAME)
                        .address(VALID_ADDRESS)
                        .detailAddress(VALID_DETAIL_ADDRESS)
                        .description(VALID_DESCRIPTION)
                        .location(Location.of(VALID_LATITUDE, VALID_LATITUDE))
                        .build()
        );
    }

    @DisplayName("픽존 생성 시 이름이 null 또는 빈 문자열인 경우 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowException_WhenNameIsNullOrEmpty(String name) {
        assertThrows(IllegalArgumentException.class, () -> PickZone.builder()
                .name(name)
                .address(VALID_ADDRESS)
                .detailAddress(VALID_DETAIL_ADDRESS)
                .description(VALID_DESCRIPTION)
                .location(Location.of(VALID_LATITUDE, VALID_LATITUDE))
                .build());
    }

    @DisplayName("픽존 생성 시 주소가 null 또는 빈 문자열인 경우 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowException_WhenAddressIsNullOrEmpty(String address) {
        assertThrows(IllegalArgumentException.class, () -> PickZone.builder()
                .name(VALID_NAME)
                .address(address)
                .detailAddress(VALID_DETAIL_ADDRESS)
                .description(VALID_DESCRIPTION)
                .location(Location.of(VALID_LATITUDE, VALID_LATITUDE))
                .build());
    }

    @DisplayName("픽존 생성 시 상세 주소가 null 또는 빈 문자열인 경우 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowException_WhenDetailAddressIsNullOrEmpty(String detailAddress) {
        assertThrows(IllegalArgumentException.class, () -> PickZone.builder()
                .name(VALID_NAME)
                .address(VALID_ADDRESS)
                .detailAddress(detailAddress)
                .description(VALID_DESCRIPTION)
                .location(Location.of(VALID_LATITUDE, VALID_LATITUDE))
                .build());
    }

    @DisplayName("픽존 생성 시 설명이 null 또는 빈 문자열인 경우 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowException_WhenDescriptionIsNullOrEmpty(String description) {
        assertThrows(IllegalArgumentException.class, () -> PickZone.builder()
                .name(VALID_NAME)
                .address(VALID_ADDRESS)
                .detailAddress(VALID_DETAIL_ADDRESS)
                .description(description)
                .location(Location.of(VALID_LATITUDE, VALID_LATITUDE))
                .build());
    }

    @DisplayName("픽존 생성 시 위치가 null인 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenLocationIsNull() {
        assertThrows(IllegalArgumentException.class, () -> PickZone.builder()
                .name(VALID_NAME)
                .address(VALID_ADDRESS)
                .detailAddress(VALID_DETAIL_ADDRESS)
                .description(VALID_DESCRIPTION)
                .location(null)
                .build());
    }

    @DisplayName("픽존 수정 시 이름이 null 또는 빈 문자열인 경우 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowException_WhenUpdateNameIsNullOrEmpty(String name) {
        PickZone pickZone = PickZone.builder()
                .name(VALID_NAME)
                .address(VALID_ADDRESS)
                .detailAddress(VALID_DETAIL_ADDRESS)
                .description(VALID_DESCRIPTION)
                .location(Location.of(VALID_LATITUDE, VALID_LATITUDE))
                .build();

        assertThrows(IllegalArgumentException.class, () -> pickZone.update(
                name,
                VALID_ADDRESS,
                VALID_DETAIL_ADDRESS,
                VALID_DESCRIPTION,
                Location.of(VALID_LATITUDE, VALID_LATITUDE)
        ));
    }

    @DisplayName("픽존 수정 시 주소가 null 또는 빈 문자열인 경우 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowException_WhenUpdateAddressIsNullOrEmpty(String address) {
        PickZone pickZone = PickZone.builder()
                .name(VALID_NAME)
                .address(VALID_ADDRESS)
                .detailAddress(VALID_DETAIL_ADDRESS)
                .description(VALID_DESCRIPTION)
                .location(Location.of(VALID_LATITUDE, VALID_LATITUDE))
                .build();

        assertThrows(IllegalArgumentException.class, () -> pickZone.update(
                VALID_NAME,
                address,
                VALID_DETAIL_ADDRESS,
                VALID_DESCRIPTION,
                Location.of(VALID_LATITUDE, VALID_LATITUDE)
        ));
    }

    @DisplayName("픽존 수정 시 상세 주소가 null 또는 빈 문자열인 경우 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowException_WhenUpdateDetailAddressIsNullOrEmpty(String detailAddress) {
        PickZone pickZone = PickZone.builder()
                .name(VALID_NAME)
                .address(VALID_ADDRESS)
                .detailAddress(VALID_DETAIL_ADDRESS)
                .description(VALID_DESCRIPTION)
                .location(Location.of(VALID_LATITUDE, VALID_LATITUDE))
                .build();

        assertThrows(IllegalArgumentException.class, () -> pickZone.update(
                VALID_NAME,
                VALID_ADDRESS,
                detailAddress,
                VALID_DESCRIPTION,
                Location.of(VALID_LATITUDE, VALID_LATITUDE)
        ));
    }

    @DisplayName("픽존 수정 시 설명이 null 또는 빈 문자열인 경우 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowException_WhenUpdateDescriptionIsNullOrEmpty(String description) {
        PickZone pickZone = PickZone.builder()
                .name(VALID_NAME)
                .address(VALID_ADDRESS)
                .detailAddress(VALID_DETAIL_ADDRESS)
                .description(VALID_DESCRIPTION)
                .location(Location.of(VALID_LATITUDE, VALID_LATITUDE))
                .build();

        assertThrows(IllegalArgumentException.class, () -> pickZone.update(
                VALID_NAME,
                VALID_ADDRESS,
                VALID_DETAIL_ADDRESS,
                description,
                Location.of(VALID_LATITUDE, VALID_LATITUDE)
        ));
    }

    @DisplayName("픽존 수정 시 위치가 null인 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenUpdateLocationIsNull() {
        PickZone pickZone = PickZone.builder()
                .name(VALID_NAME)
                .address(VALID_ADDRESS)
                .detailAddress(VALID_DETAIL_ADDRESS)
                .description(VALID_DESCRIPTION)
                .location(Location.of(VALID_LATITUDE, VALID_LATITUDE))
                .build();

        assertThrows(IllegalArgumentException.class, () -> pickZone.update(
                VALID_NAME,
                VALID_ADDRESS,
                VALID_DETAIL_ADDRESS,
                VALID_DESCRIPTION,
                null
        ));
    }

    @DisplayName("픽존 수정 시 이름이 변경되면 이름이 변경된다.")
    @Test
    void shouldChangeName_WhenUpdateName() {
        PickZone pickZone = PickZone.builder()
                .name(VALID_NAME)
                .address(VALID_ADDRESS)
                .detailAddress(VALID_DETAIL_ADDRESS)
                .description(VALID_DESCRIPTION)
                .location(Location.of(VALID_LATITUDE, VALID_LATITUDE))
                .build();

        String updatedName = "updated name";
        pickZone.update(
                updatedName,
                VALID_ADDRESS,
                VALID_DETAIL_ADDRESS,
                VALID_DESCRIPTION,
                Location.of(VALID_LATITUDE, VALID_LATITUDE)
        );

        assertEquals(updatedName, pickZone.getName());
    }

    @DisplayName("픽존 수정 시 주소가 변경되면 주소가 변경된다.")
    @Test
    void shouldChangeAddress_WhenUpdateAddress() {
        PickZone pickZone = PickZone.builder()
                .name(VALID_NAME)
                .address(VALID_ADDRESS)
                .detailAddress(VALID_DETAIL_ADDRESS)
                .description(VALID_DESCRIPTION)
                .location(Location.of(VALID_LATITUDE, VALID_LATITUDE))
                .build();

        String updatedAddress = "updated address";
        pickZone.update(
                VALID_NAME,
                updatedAddress,
                VALID_DETAIL_ADDRESS,
                VALID_DESCRIPTION,
                Location.of(VALID_LATITUDE, VALID_LATITUDE)
        );

        assertEquals(updatedAddress, pickZone.getAddress());
    }

    @DisplayName("픽존 수정 시 상세 주소가 변경되면 상세 주소가 변경된다.")
    @Test
    void shouldChangeDetailAddress_WhenUpdateDetailAddress() {
        PickZone pickZone = PickZone.builder()
                .name(VALID_NAME)
                .address(VALID_ADDRESS)
                .detailAddress(VALID_DETAIL_ADDRESS)
                .description(VALID_DESCRIPTION)
                .location(Location.of(VALID_LATITUDE, VALID_LATITUDE))
                .build();

        String updatedDetailAddress = "updated detail address";
        pickZone.update(
                VALID_NAME,
                VALID_ADDRESS,
                updatedDetailAddress,
                VALID_DESCRIPTION,
                Location.of(VALID_LATITUDE, VALID_LATITUDE)
        );

        assertEquals(updatedDetailAddress, pickZone.getDetailAddress());
    }

    @DisplayName("픽존 수정 시 설명이 변경되면 설명이 변경된다.")
    @Test
    void shouldChangeDescription_WhenUpdateDescription() {
        PickZone pickZone = PickZone.builder()
                .name(VALID_NAME)
                .address(VALID_ADDRESS)
                .detailAddress(VALID_DETAIL_ADDRESS)
                .description(VALID_DESCRIPTION)
                .location(Location.of(VALID_LATITUDE, VALID_LATITUDE))
                .build();

        String updatedDescription = "updated description";
        pickZone.update(
                VALID_NAME,
                VALID_ADDRESS,
                VALID_DETAIL_ADDRESS,
                updatedDescription,
                Location.of(VALID_LATITUDE, VALID_LATITUDE)
        );

        assertEquals(updatedDescription, pickZone.getDescription());
    }

    @DisplayName("픽존 수정 시 위치가 변경되면 위치가 변경된다.")
    @Test
    void shouldChangeLocation_WhenUpdateLocation() {
        PickZone pickZone = PickZone.builder()
                .name(VALID_NAME)
                .address(VALID_ADDRESS)
                .detailAddress(VALID_DETAIL_ADDRESS)
                .description(VALID_DESCRIPTION)
                .location(Location.of(VALID_LATITUDE, VALID_LATITUDE))
                .build();

        Location updatedLocation = Location.of(0.0, 0.0);
        pickZone.update(
                VALID_NAME,
                VALID_ADDRESS,
                VALID_DETAIL_ADDRESS,
                VALID_DESCRIPTION,
                updatedLocation
        );

        assertEquals(updatedLocation, pickZone.getLocation());
    }
}
