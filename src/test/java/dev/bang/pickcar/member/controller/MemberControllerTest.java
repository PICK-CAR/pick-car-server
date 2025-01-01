package dev.bang.pickcar.member.controller;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

import dev.bang.pickcar.member.MemberTestHelper;
import dev.bang.pickcar.member.entity.Member;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@DisplayName("MemberController 테스트")
@ActiveProfiles("test")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MemberControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MemberTestHelper memberTestHelper;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("내 정보 조회 테스트")
    @Test
    void me() {
        Member member = memberTestHelper.createMember();
        String accessToken = memberTestHelper.getAccessTokenFromMember(member);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .when().get("members/me")
                .then().log().all()
                .statusCode(200);
    }
}
