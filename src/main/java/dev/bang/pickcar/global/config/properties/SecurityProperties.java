package dev.bang.pickcar.global.config.properties;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security")
public record SecurityProperties(
        List<String> whitelist,
        Jwt jwt
) {
    public record Jwt(
            String secretKey,
            long expirationTime
    ) {
    }
}
