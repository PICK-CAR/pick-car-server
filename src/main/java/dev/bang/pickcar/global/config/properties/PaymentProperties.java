package dev.bang.pickcar.global.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "payment")
public record PaymentProperties(
        String secretKey,
        String baseUrl,
        String confirmEndpoint,
        String cancelEndpoint
) {
    public String getConfirmUrl() {
        return baseUrl + confirmEndpoint;
    }

    public String getCancelUrl(String paymentKey) {
        return baseUrl + "/" + paymentKey + cancelEndpoint;
    }
}
