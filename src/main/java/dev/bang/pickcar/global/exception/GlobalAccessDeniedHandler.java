package dev.bang.pickcar.global.exception;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * Spring Security, 접근 권한이 없는 경우 발생하는 예외를 처리하는 클래스입니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GlobalAccessDeniedHandler implements AccessDeniedHandler {

    private final Gson gson;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        log.warn("AccessDeniedException: {}", accessDeniedException.getMessage());
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN.value(), "접근 권한이 없습니다.");
        response.getWriter().write(gson.toJson(errorResponse));
    }
}
