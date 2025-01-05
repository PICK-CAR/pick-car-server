package dev.bang.pickcar.pickzone.dto;

import jakarta.validation.constraints.NotBlank;

public record PickZoneRequest(
        @NotBlank(message = "픽존 이름을 입력해주세요.")
        String name,
        @NotBlank(message = "주소를 입력해주세요.")
        String address,
        @NotBlank(message = "상세 주소를 입력해주세요.")
        String detailAddress,
        @NotBlank(message = "설명을 입력해주세요.")
        String description,
        double latitude,
        double longitude
) {
}
