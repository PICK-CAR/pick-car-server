package dev.bang.pickcar.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "swagger")
public record SwaggerProperties(
        String localServerUrl,
        String devServerUrl
) {
}
