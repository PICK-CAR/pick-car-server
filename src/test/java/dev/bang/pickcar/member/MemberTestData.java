package dev.bang.pickcar.member;

import static dev.bang.pickcar.member.MemberConstant.PHONE_NUMBER_FORMAT;

import dev.bang.pickcar.auth.dto.LoginRequest;
import dev.bang.pickcar.member.dto.MemberRequest;
import java.time.LocalDate;

public class MemberTestData {

    public static final String VALID_NAME = "홍길동";
    public static final String VALID_NICKNAME = "홍홍홍";
    public static final String VALID_EMAIL = "hong@gmail.com";
    public static final String VALID_PASSWORD = "5E884898DA28047151D0E56F8DC6292773603D0D6AABBDD62A11EF721D1542D8";
    public static final LocalDate VALID_BIRTHDAY = LocalDate.of(2000, 1, 1);
    public static final String VALID_PHONE_NUMBER = PHONE_NUMBER_FORMAT;

    private MemberTestData() {
    }

    public static MemberRequest createMemberRequest() {
        return new MemberRequest(
                VALID_NAME,
                VALID_NICKNAME,
                VALID_EMAIL,
                VALID_PASSWORD,
                VALID_BIRTHDAY,
                VALID_PHONE_NUMBER
        );
    }

    public static MemberRequest createCustomMemberRequest(Object... args) {
        return new MemberRequest(
                (String) args[0],
                (String) args[1],
                (String) args[2],
                (String) args[3],
                (LocalDate) args[4],
                (String) args[5]
        );
    }

    public static LoginRequest createLoginRequest() {
        return new LoginRequest(
                VALID_EMAIL,
                VALID_PASSWORD
        );
    }
}
