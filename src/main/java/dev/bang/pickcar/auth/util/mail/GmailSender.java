package dev.bang.pickcar.auth.util.mail;

import dev.bang.pickcar.global.config.properties.MailProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
@EnableConfigurationProperties(MailProperties.class)
public class GmailSender implements MailSender {

    private final JavaMailSender javaMailSender;

    @Override
    public void send(String to, String subject, String text) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        try {
            simpleMailMessage.setTo(to);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(text);
            javaMailSender.send(simpleMailMessage);
            log.info("메일 발송을 완료했습니다.");
        } catch (Exception e) {
            log.info("메일 발송 실패: {}", e.getMessage());
            throw new IllegalStateException("메일 발송에 실패했습니다.");
        }
    }
}
