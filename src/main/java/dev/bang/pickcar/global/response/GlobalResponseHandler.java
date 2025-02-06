package dev.bang.pickcar.global.response;

import dev.bang.pickcar.global.exception.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    private static final String APPLICATION_PACKAGE = "dev.bang.pickcar";

    @Override
    public boolean supports(@NonNull MethodParameter returnType,
                            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return !isHandlingException(returnType)
                && isClassInTargetPackage(returnType);
    }

    private boolean isHandlingException(MethodParameter returnType) {
        return returnType.getContainingClass()
                .equals(GlobalExceptionHandler.class);
    }

    private boolean isClassInTargetPackage(MethodParameter returnType) {
        return returnType.getContainingClass()
                .getName()
                .startsWith(APPLICATION_PACKAGE);
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request,
                                  @NonNull ServerHttpResponse response) {
        if (response instanceof ServletServerHttpResponse servletResponse) {
            int statusCode = servletResponse.getServletResponse().getStatus();
            return ApiResponse.of(statusCode, body);
        }
        return ApiResponse.success(body);
    }
}
