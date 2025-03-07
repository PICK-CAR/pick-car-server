package dev.bang.pickcar.auth.controller;

import static dev.bang.pickcar.member.MemberTestData.VALID_BIRTHDAY;
import static dev.bang.pickcar.member.MemberTestData.VALID_EMAIL;
import static dev.bang.pickcar.member.MemberTestData.VALID_NAME;
import static dev.bang.pickcar.member.MemberTestData.VALID_NICKNAME;
import static dev.bang.pickcar.member.MemberTestData.VALID_PASSWORD;
import static dev.bang.pickcar.member.MemberTestData.VALID_PHONE_NUMBER;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

import dev.bang.pickcar.auth.dto.LoginRequest;
import dev.bang.pickcar.member.MemberTestHelper;
import dev.bang.pickcar.member.dto.MemberRequest;
import dev.bang.pickcar.member.entity.Member;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@DisplayName("AuthController 테스트")
@ActiveProfiles("test")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AuthControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MemberTestHelper memberTestHelper;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("회원가입 테스트")
    @Test
    void signup() {
        MemberRequest signupRequest = memberTestHelper.createMemberRequest();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(signupRequest)
                .when().post("auth/signup")
                .then().log().all()
                .statusCode(201)
                .header(HttpHeaders.LOCATION, "members/1");
    }

    @DisplayName("잘못된 이름 형식으로 회원가입 요청을 보내면 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowException_WhenNameIsInvalid(String invalidName) {
        MemberRequest signupRequest = memberTestHelper.createCustomMemberRequest(
                invalidName,
                VALID_NICKNAME,
                VALID_EMAIL,
                VALID_PASSWORD,
                VALID_BIRTHDAY,
                VALID_PHONE_NUMBER
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(signupRequest)
                .when().post("auth/signup")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("잘못된 닉네임 형식으로 회원가입 요청을 보내면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "\n", "\t", "\r"})
    void shouldThrowException_WhenRequestNicknameIsInvalid(String invalidNickname) {
        MemberRequest signupRequest = memberTestHelper.createCustomMemberRequest(
                VALID_NAME,
                invalidNickname,
                VALID_EMAIL,
                VALID_PASSWORD,
                VALID_BIRTHDAY,
                VALID_PHONE_NUMBER
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(signupRequest)
                .when().post("auth/signup")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("잘못된 이메일 형식으로 회원가입 요청을 보내면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "\n", "\t", "\r", "invalid", "invalid.com", "invalid@com"})
    void shouldThrowException_WhenRequestEmailIsInvalid(String invalidEmail) {
        MemberRequest signupRequest = memberTestHelper.createCustomMemberRequest(
                VALID_NAME,
                VALID_NICKNAME,
                invalidEmail,
                VALID_PASSWORD,
                VALID_BIRTHDAY,
                VALID_PHONE_NUMBER
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(signupRequest)
                .when().post("auth/signup")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("잘못된 휴대폰 번호 형식으로 회원가입 요청을 보내면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "\n", "\t", "\r", "invalid",  "0100000-0000", "010-00000000", "010-0000-0000"})
    void shouldThrowException_WhenRequestPhoneNumberIsInvalid(String invalidPhoneNumber) {
        MemberRequest signupRequest = memberTestHelper.createCustomMemberRequest(
                VALID_NAME,
                VALID_NICKNAME,
                VALID_EMAIL,
                VALID_PASSWORD,
                VALID_BIRTHDAY,
                invalidPhoneNumber
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(signupRequest)
                .when().post("auth/signup")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("로그인 테스트")
    @Test
    void login() {
        MemberRequest signupRequest = memberTestHelper.createMemberRequest();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(signupRequest)
                .when().post("auth/signup")
                .then().log().all()
                .statusCode(201);

        LoginRequest loginRequest = memberTestHelper.createLoginRequest();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when().post("auth/login")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("로그인 요청 본문이 올바르지 않은 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {
            " , " + VALID_PASSWORD,
            VALID_EMAIL + ", ",
    })
    void shouldThrowException_WhenLoginRequestBodyIsInvalid(String invalidRequest) {
        String[] requestParams = invalidRequest.split(",");
        LoginRequest loginRequest = new LoginRequest(requestParams[0], requestParams[1]);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when().post("auth/login")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("중복되는 이메일이 존재하는지 확인한다.")
    @Test
    void checkEmailDuplication() {
        // 1. 이메일이 존재하지 않는 경우 → false 반환
        Boolean result = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().post("auth/check/email/{email}", VALID_EMAIL)
                .then().log().all()
                .statusCode(200)
                .extract().as(Boolean.class);
        Assertions.assertFalse(result);

        // 2. 회원을 생성하고 해당 회원의 이메일로 중복 확인
        Member member = memberTestHelper.createMember();

        // 3. 이메일이 존재하는 경우 → true 반환
        result = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().post("auth/check/email/{email}", member.getEmail())
                .then().log().all()
                .statusCode(200)
                .extract().as(Boolean.class);
        Assertions.assertTrue(result);
    }

    @DisplayName("이메일로 인증번호를 전송한다.")
    @Test
    void sendVerificationCodeToEmail() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().post("auth/verification/send/email/{email}", VALID_EMAIL)
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("이메일로 인증번호를 확인한다.")
    @Test
    void verifyEmail() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(memberTestHelper.createEmailVerifyRequest())
                .when().post("auth/verification/verify/email")
                .then().log().all()
                .statusCode(200);
    }
}
