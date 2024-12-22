package dev.bang.pickcar.member.entity;

import static dev.bang.pickcar.member.MemberConstant.MAX_NICKNAME_LENGTH;
import static dev.bang.pickcar.member.MemberConstant.MIN_NICKNAME_LENGTH;
import static dev.bang.pickcar.member.MemberTestData.VALID_BIRTHDAY;
import static dev.bang.pickcar.member.MemberTestData.VALID_EMAIL;
import static dev.bang.pickcar.member.MemberTestData.VALID_NAME;
import static dev.bang.pickcar.member.MemberTestData.VALID_NICKNAME;
import static dev.bang.pickcar.member.MemberTestData.VALID_PASSWORD;
import static dev.bang.pickcar.member.MemberTestData.VALID_PHONE_NUMBER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("회원 테스트")
class MemberTest {

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

    @DisplayName("회원 생성 시 닉네임 길이가 규칙에 맞지 않는 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {MIN_NICKNAME_LENGTH - 1, MAX_NICKNAME_LENGTH + 1})
    void shouldThrowException_WhenNicknameIsNotValid(int nicknameLength) {
        String nickname = "a".repeat(nicknameLength);
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
    @ValueSource(strings = {"010-0000-0000", "01100000000", "0100000000000", "032-000-0000"})
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
