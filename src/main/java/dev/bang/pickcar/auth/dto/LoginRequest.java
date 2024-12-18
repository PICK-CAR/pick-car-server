package dev.bang.pickcar.auth.dto;

public record LoginRequest(
        String email,
        String password
) {
}
