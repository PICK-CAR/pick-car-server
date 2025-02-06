package dev.bang.pickcar.global.exception;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        int status,
        String message
) {
    public static ErrorResponse badRequest(String message) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message);
    }

    public static ErrorResponse of(int status, String message) {
        return new ErrorResponse(status, message);
    }
}
