package dev.bang.pickcar.global.config;

import dev.bang.pickcar.global.config.properties.SpringMailProperties;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(SpringMailProperties.class)
public class SpringMailConfig {

    private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    private static final String MAIL_DEBUG = "mail.smtp.debug";
    private static final String MAIL_CONNECTION_TIMEOUT = "mail.smtp.connectiontimeout";
    private static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    private static final String DEFAULT_ENCODING = "UTF-8";

    private final SpringMailProperties springMailProperties;

    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(springMailProperties.host());
        javaMailSender.setUsername(springMailProperties.username());
        javaMailSender.setPassword(springMailProperties.password());
        javaMailSender.setPort(springMailProperties.port());

        Properties properties = javaMailSender.getJavaMailProperties();
        properties.put(MAIL_SMTP_AUTH, springMailProperties.properties().mail().smtp().auth());
        properties.put(MAIL_DEBUG, springMailProperties.properties().mail().smtp().debug());
        properties.put(MAIL_CONNECTION_TIMEOUT, springMailProperties.properties().mail().smtp().connectionTimeout());
        properties.put(MAIL_SMTP_STARTTLS_ENABLE, springMailProperties.properties().mail().smtp().starttls().enable());

        javaMailSender.setJavaMailProperties(properties);
        javaMailSender.setDefaultEncoding(DEFAULT_ENCODING);

        return javaMailSender;
    }
}
