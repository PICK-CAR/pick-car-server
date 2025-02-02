package dev.bang.pickcar.member;

import static dev.bang.pickcar.member.MemberTestData.ADMIN_NAME;
import static dev.bang.pickcar.member.MemberTestData.ADMIN_PASSWORD;
import static dev.bang.pickcar.member.MemberTestData.ADMIN_PHONE_NUMBER;
import static dev.bang.pickcar.member.MemberTestData.VALID_BIRTHDAY;
import static dev.bang.pickcar.member.MemberTestData.VALID_EMAIL;
import static dev.bang.pickcar.member.MemberTestData.VALID_NAME;
import static dev.bang.pickcar.member.MemberTestData.VALID_NICKNAME;
import static dev.bang.pickcar.member.MemberTestData.VALID_PASSWORD;
import static dev.bang.pickcar.member.MemberTestData.VALID_PHONE_NUMBER;
import static dev.bang.pickcar.member.MemberTestData.VERIFICATION_CODE;

import dev.bang.pickcar.auth.dto.EmailVerifyRequest;
import dev.bang.pickcar.auth.dto.LoginRequest;
import dev.bang.pickcar.auth.dto.MemberAuthResponse;
import dev.bang.pickcar.auth.jwt.TokenProvider;
import dev.bang.pickcar.member.dto.MemberRequest;
import dev.bang.pickcar.member.entity.Member;
import dev.bang.pickcar.member.entity.MemberRole;
import dev.bang.pickcar.member.repository.MemberRepository;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Component
@ActiveProfiles("test")
public class MemberTestHelper {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TokenProvider tokenProvider;

    private final AtomicLong counter = new AtomicLong();

    public MemberRequest createMemberRequest() {
        return new MemberRequest(
                VALID_NAME,
                VALID_NICKNAME,
                VALID_EMAIL,
                VALID_PASSWORD,
                VALID_BIRTHDAY,
                VALID_PHONE_NUMBER
        );
    }

    public MemberRequest createCustomMemberRequest(Object... args) {
        return new MemberRequest(
                (String) args[0],
                (String) args[1],
                (String) args[2],
                (String) args[3],
                (LocalDate) args[4],
                (String) args[5]
        );
    }

    public LoginRequest createLoginRequest() {
        return new LoginRequest(
                VALID_EMAIL,
                VALID_PASSWORD
        );
    }

    @Transactional
    public Member createMember() {
        long uniqueValue = counter.incrementAndGet();
        String uniqueNickname = String.format("tester%d", uniqueValue);
        String uniqueEmail = String.format("test%d@example.com", uniqueValue);
        return memberRepository.save(
                new Member(
                        VALID_NAME,
                        uniqueNickname,
                        uniqueEmail,
                        VALID_PASSWORD,
                        VALID_BIRTHDAY,
                        VALID_PHONE_NUMBER
                )
        );
    }

    @Transactional
    public Member createAdmin() {
        long uniqueValue = counter.incrementAndGet();
        String uniqueNickname = String.format("admin%d", uniqueValue);
        String uniqueEmail = String.format("admin%d@example.com", uniqueValue);
        return memberRepository.save(
                new Member(
                        ADMIN_NAME,
                        uniqueNickname,
                        uniqueEmail,
                        ADMIN_PASSWORD,
                        ADMIN_PHONE_NUMBER,
                        MemberRole.ADMIN
                )
        );
    }

    public String getAccessTokenFromMember(Member member) {
        MemberAuthResponse authResponse = new MemberAuthResponse(member.getId(), member.getRole().name());
        return tokenProvider.generateToken(authResponse)
                .accessToken();
    }

    public EmailVerifyRequest createEmailVerifyRequest() {
        return new EmailVerifyRequest(VALID_EMAIL, VERIFICATION_CODE);
    }
}
