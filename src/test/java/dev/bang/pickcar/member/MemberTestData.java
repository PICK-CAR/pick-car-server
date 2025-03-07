package dev.bang.pickcar.member;

import static dev.bang.pickcar.member.MemberConstant.PHONE_NUMBER_FORMAT;

import java.time.LocalDate;

public class MemberTestData {

    public static final String VALID_NAME = "홍길동";
    public static final String VALID_NICKNAME = "홍홍홍";
    public static final String VALID_EMAIL = "hong@gmail.com";
    public static final String VALID_PASSWORD = "5E884898DA28047151D0E56F8DC6292773603D0D6AABBDD62A11EF721D1542D8";
    public static final LocalDate VALID_BIRTHDAY = LocalDate.of(2000, 1, 1);
    public static final String VALID_PHONE_NUMBER = PHONE_NUMBER_FORMAT;

    public static final String ADMIN_NAME = "관리자";
    public static final String ADMIN_NICKNAME = "admin";
    public static final String ADMIN_EMAIL = "admin@gmail.com";
    public static final String ADMIN_PASSWORD = "5E884898DA28047151D0E56F8DC6292773603D0D6AABBDD62A11EF721D1542D8";
    public static final String ADMIN_PHONE_NUMBER = "01000001234";

    public static final String VERIFICATION_CODE = "123456";

    private MemberTestData() {
    }
}
