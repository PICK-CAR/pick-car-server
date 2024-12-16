package dev.bang.pickcar.entitiy.member;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("회원 테스트")
class MemberTest {

    private static final String VALID_NAME = "홍길동";
    private static final String VALID_NICKNAME = "홍홍홍";
    private static final String VALID_EMAIL = "hong@gmail.com";
    private static final String VALID_PASSWORD = "5E884898DA28047151D0E56F8DC6292773603D0D6AABBDD62A11EF721D1542D8";
    private static final LocalDate VALID_BIRTHDAY = LocalDate.of(2000, 1, 1);
    private static final String VALID_PHONE_NUMBER = "010-0000-0000";

    @DisplayName("정상적인 회원 생성은 예외가 발생하지 않는다.")
    @Test
    void shouldNotThrowException_WhenValidCreate() {
        assertThatCode(() ->
                new Member(
                        VALID_NAME,
                        VALID_NICKNAME,
                        VALID_EMAIL,
                        VALID_PASSWORD,
                        VALID_BIRTHDAY,
                        VALID_PHONE_NUMBER
                )
        ).doesNotThrowAnyException();
    }

    @DisplayName("회원 생성 시 역할을 지정하지 않으면 기본값으로 MEMBER 역할이 지정된다.")
    @Test
    void shouldSetDefaultRole_WhenRoleIsNotSpecified() {
        assertThatCode(() -> {
            Member member = new Member(
                    VALID_NAME,
                    VALID_NICKNAME,
                    VALID_EMAIL,
                    VALID_PASSWORD,
                    VALID_BIRTHDAY,
                    VALID_PHONE_NUMBER
            );

            assertThat(member.getRole()).isEqualTo(MemberRole.MEMBER);
        }).doesNotThrowAnyException();
    }

    @DisplayName("회원 생성 시 이름이 null 또는 빈 문자열인 경우 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowException_WhenNameIsNullOrEmpty(String name) {
        assertThatThrownBy(() ->
                new Member(
                        name,
                        VALID_NICKNAME,
                        VALID_EMAIL,
                        VALID_PASSWORD,
                        VALID_BIRTHDAY,
                        VALID_PHONE_NUMBER
                )
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("회원 생성 시 닉네임이 null 또는 빈 문자열인 경우 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowException_WhenNicknameIsNullOrEmpty(String nickname) {
        assertThatThrownBy(() ->
                new Member(
                        VALID_NAME,
                        nickname,
                        VALID_EMAIL,
                        VALID_PASSWORD,
                        VALID_BIRTHDAY,
                        VALID_PHONE_NUMBER
                )
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("회원 생성 시 닉네임이 2~10자가 아닌 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"난", "닉네임이11자가넘어가"})
    void shouldThrowException_WhenNicknameIsNotValid(String nickname) {
        assertThatThrownBy(() ->
                new Member(
                        VALID_NAME,
                        nickname,
                        VALID_EMAIL,
                        VALID_PASSWORD,
                        VALID_BIRTHDAY,
                        VALID_PHONE_NUMBER
                )
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("회원 생성 시 이메일이 null 또는 빈 문자열인 경우 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowException_WhenEmailIsNullOrEmpty(String email) {
        assertThatThrownBy(() ->
                new Member(
                        VALID_NAME,
                        VALID_NICKNAME,
                        email,
                        VALID_PASSWORD,
                        VALID_BIRTHDAY,
                        VALID_PHONE_NUMBER
                )
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("회원 생성 시 이메일 형식이 올바르지 않은 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"invalid", "invalid.com", "invalid@com"})
    void shouldThrowException_WhenEmailIsNotValid(String email) {
        assertThatThrownBy(() ->
                new Member(
                        VALID_NAME,
                        VALID_NICKNAME,
                        email,
                        VALID_PASSWORD,
                        VALID_BIRTHDAY,
                        VALID_PHONE_NUMBER
                )
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("회원 생성 시 비밀번호가 null 또는 빈 문자열인 경우 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowException_WhenPasswordIsNullOrEmpty(String password) {
        assertThatThrownBy(() ->
                new Member(
                        VALID_NAME,
                        VALID_NICKNAME,
                        VALID_EMAIL,
                        password,
                        VALID_BIRTHDAY,
                        VALID_PHONE_NUMBER
                )
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("회원 생성 시 휴대폰 번호가 null 또는 빈 문자열인 경우 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowException_WhenPhoneNumberIsNullOrEmpty(String phoneNumber) {
        assertThatThrownBy(() ->
                new Member(
                        VALID_NAME,
                        VALID_NICKNAME,
                        VALID_EMAIL,
                        VALID_PASSWORD,
                        VALID_BIRTHDAY,
                        phoneNumber
                )
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("회원 생성 시 휴대폰 번호 형식이 올바르지 않은 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"01000000000", "010-0000-000", "010-0000-00000", "010-0000-000A"})
    void shouldThrowException_WhenPhoneNumberIsNotValid(String phoneNumber) {
        assertThatThrownBy(() ->
                new Member(
                        VALID_NAME,
                        VALID_NICKNAME,
                        VALID_EMAIL,
                        VALID_PASSWORD,
                        VALID_BIRTHDAY,
                        phoneNumber
                )
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
