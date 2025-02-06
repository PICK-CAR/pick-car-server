package dev.bang.pickcar.global.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mail")
public record MailProperties(
        String imapServer,
        int imapPort,
        String account,
        String password
) {
}
