package dev.bang.pickcar.global.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "swagger")
public record SwaggerProperties(
        String localServerUrl,
        String devServerUrl
) {
}
