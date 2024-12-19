package dev.bang.pickcar.auth.dto;

public record MemberAuthResponse(
        Long memberId,
        String role
) {
}
