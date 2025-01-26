package dev.bang.pickcar.payment.external;

import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;

public class PaymentExceptionInterceptor implements ClientHttpRequestInterceptor {

    @NonNull
    @Override
    public ClientHttpResponse intercept(@NonNull HttpRequest request,
                                        @NonNull byte[] body,
                                        @NonNull ClientHttpRequestExecution execution) {
        try {
            return execution.execute(request, body);
        } catch (IOException e) {
            throw new PaymentException("결제 입출력 대기 관련 오류가 발생했습니다. " + e.getMessage());
        } catch (Exception e) {
            throw new PaymentException("결제 과정 중 오류가 발생했습니다. " + e.getMessage());
        }
    }
}
