package dev.bang.pickcar.global.response;

import org.springframework.http.HttpStatus;

public record ApiResponse(
        int status,
        Object data
) {
    public static ApiResponse success(Object data) {
        return new ApiResponse(HttpStatus.OK.value(), data);
    }

    public static ApiResponse of(int status, Object data) {
        return new ApiResponse(status, data);
    }
}
