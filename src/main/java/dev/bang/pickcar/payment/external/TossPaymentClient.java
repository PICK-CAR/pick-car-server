package dev.bang.pickcar.payment.external;

import com.google.gson.Gson;
import dev.bang.pickcar.config.properties.PaymentProperties;
import dev.bang.pickcar.payment.dto.RequestPaymentCancel;
import dev.bang.pickcar.payment.dto.RequestPaymentConfirm;
import dev.bang.pickcar.payment.dto.ResponsePaymentCancel;
import dev.bang.pickcar.payment.dto.ResponsePaymentConfirm;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Profile("!test")
@EnableConfigurationProperties(PaymentProperties.class)
public class TossPaymentClient implements PaymentClient {

    private static final String BASIC_DELIMITER = ":";
    private static final String AUTH_HEADER_PREFIX = "Basic ";
    private static final int CONNECT_TIMEOUT_SECONDS = 1;
    private static final int READ_TIMEOUT_SECONDS = 30;
    private static final int MILLIS_IN_SECOND = 1_000;

    private final PaymentProperties paymentProperties;
    private final RestClient restClient;

    public TossPaymentClient(PaymentProperties paymentProperties) {
        this.paymentProperties = paymentProperties;
        this.restClient = initRestClient();
    }

    private RestClient initRestClient() {
        return RestClient.builder()
                .requestFactory(getClientHttpRequestFactory())
                .requestInterceptor(new PaymentExceptionInterceptor())
                .requestInterceptor(new PaymentLoggingInterceptor())
                .defaultHeader(HttpHeaders.AUTHORIZATION, getPaymentAuthHeader())
                .build();
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(CONNECT_TIMEOUT_SECONDS * MILLIS_IN_SECOND);
        factory.setReadTimeout(READ_TIMEOUT_SECONDS * MILLIS_IN_SECOND);
        return factory;
    }

    private String getPaymentAuthHeader() {
        byte[] encodedBytes = Base64.getEncoder()
                .encode((paymentProperties.secretKey() + BASIC_DELIMITER).getBytes(StandardCharsets.UTF_8));
        return AUTH_HEADER_PREFIX + new String(encodedBytes, StandardCharsets.UTF_8);
    }

    @Override
    public ResponsePaymentConfirm confirmPayment(RequestPaymentConfirm confirmRequest) {
        return restClient.method(HttpMethod.POST)
                .uri(paymentProperties.getConfirmUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .body(confirmRequest)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> paymentErrorHandling(response))
                .body(ResponsePaymentConfirm.class);
    }

    private void paymentErrorHandling(ClientHttpResponse response) throws IOException {
        ResponsePaymentError responsePaymentError = getResponsePaymentError(response);
        if (response.getStatusCode().is4xxClientError()) {
            throw new PaymentException("잘못된 결제 요청입니다. %s".formatted(responsePaymentError.message()));
        }
        if (response.getStatusCode().is5xxServerError()) {
            throw new PaymentException("결제 서버에서 오류가 발생했습니다. %s".formatted(responsePaymentError.message()));
        }
        throw new PaymentException("결제 요청에 실패했습니다. %s".formatted(responsePaymentError.message()));
    }

    private ResponsePaymentError getResponsePaymentError(ClientHttpResponse response) throws IOException {
        return new Gson().fromJson(response.getBody().toString(), ResponsePaymentError.class);
    }

    record ResponsePaymentError(
            String code,
            String message
    ) {
    }

    @Override
    public ResponsePaymentCancel cancelPayment(String paymentKey, RequestPaymentCancel cancelRequest) {
        return restClient.method(HttpMethod.POST)
                .uri(paymentProperties.getCancelUrl(paymentKey))
                .contentType(MediaType.APPLICATION_JSON)
                .body(cancelRequest)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> paymentErrorHandling(response))
                .body(ResponsePaymentCancel.class);
    }
}
