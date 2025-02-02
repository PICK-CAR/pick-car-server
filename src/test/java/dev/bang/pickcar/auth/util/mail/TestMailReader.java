package dev.bang.pickcar.auth.util.mail;

import static dev.bang.pickcar.member.MemberTestData.VERIFICATION_CODE;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 테스트용 메일 리더입니다.
 * 테스트 데이터인 VERIFICATION_CODE 를 반환합니다.
 */
@Component
@Profile("test")
public class TestMailReader implements MailReader {

    @Override
    public String findLatestMessageBySenderPhoneNumber(String phoneNumber) {
        return VERIFICATION_CODE;
    }
}
