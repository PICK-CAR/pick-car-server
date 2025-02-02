package dev.bang.pickcar.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.mail")
public record SpringMailProperties(
        String host,
        int port,
        String username,
        String password,
        Properties properties
) {
    public record Properties(
            Mail mail
    ) {
        public record Mail(
                Smtp smtp
        ) {
            public record Smtp(
                    boolean debug,
                    boolean auth,
                    int connectionTimeout,
                    Starttls starttls
            ) {
                public record Starttls(
                        boolean enable
                ) {
                }
            }
        }
    }
}

