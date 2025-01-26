package dev.bang.pickcar.payment.external;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;

@Slf4j
public class PaymentLoggingInterceptor implements ClientHttpRequestInterceptor {

    @NonNull
    @Override
    public ClientHttpResponse intercept(HttpRequest request,
                                        @NonNull byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        String host = request.getURI().getHost();
        String path = request.getURI().getPath();
        String httpMethod = request.getMethod().toString();
        log.info("Request to {}{} with method {} and body {}",
                host, path, httpMethod, new String(body, StandardCharsets.UTF_8));
        return execution.execute(request, body);
    }
}
