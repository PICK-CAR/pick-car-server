package dev.bang.pickcar.global.exception;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Spring Security, 인증이 필요한 경우 발생하는 예외를 처리하는 클래스입니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GlobalAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final Gson gson;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.warn("AuthenticationException: {}", authException.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.UNAUTHORIZED.value(), "인증이 필요합니다.");
        response.getWriter().write(gson.toJson(errorResponse));
    }
}
