package dev.bang.pickcar.auth.util.mail;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 테스트용 메일 센더입니다.
 * 실제로 메일을 보내지 않고, 아무 동작도 하지 않습니다.
 */
@Component
@Profile("test")
public class TestMailSender implements MailSender {

    @Override
    public void send(String to, String subject, String text) {
    }
}
