package dev.bang.pickcar.pickzone.controller;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

import dev.bang.pickcar.member.MemberTestHelper;
import dev.bang.pickcar.member.entity.Member;
import dev.bang.pickcar.pickzone.PickZoneTestHelper;
import dev.bang.pickcar.pickzone.dto.PickZoneRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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

@DisplayName("PickZoneManageController 테스트")
@ActiveProfiles("test")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PickZoneManageControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MemberTestHelper memberTestHelper;

    @Autowired
    private PickZoneTestHelper pickZoneTestHelper;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("관리자인 경우 새로운 픽존을 등록할 수 있다.")
    @Test
    void createPickZone() {
        Member admin = memberTestHelper.createAdmin();
        String accessToken = memberTestHelper.getAccessTokenFromMember(admin);

        PickZoneRequest pickZoneRequest = pickZoneTestHelper.createPickZoneRequest();

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(pickZoneRequest)
                .when().post("pick-zones")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("관리자가 아닌 경우 새로운 픽존을 등록할 수 없다.")
    @Test
    void createPickZone_Unauthorized() {
        Member member = memberTestHelper.createMember();
        String accessToken = memberTestHelper.getAccessTokenFromMember(member);

        PickZoneRequest pickZoneRequest = pickZoneTestHelper.createPickZoneRequest();

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(pickZoneRequest)
                .when().post("pick-zones")
                .then().log().all()
                .statusCode(403);
    }

    @DisplayName("관리자인 경우 픽존을 수정할 수 있다.")
    @Test
    void updatePickZone() {
        Member admin = memberTestHelper.createAdmin();
        String accessToken = memberTestHelper.getAccessTokenFromMember(admin);

        PickZoneRequest pickZoneRequest = pickZoneTestHelper.createPickZoneRequest();
        long pickZoneId = pickZoneTestHelper.createPickZone(pickZoneRequest);

        PickZoneRequest updatePickZoneRequest = new PickZoneRequest(
                "수정된 픽존 이름",
                "수정된 주소",
                "수정된 상세 주소",
                "수정된 설명",
                37.123456,
                127.123456
        );

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(updatePickZoneRequest)
                .when().put("pick-zones/" + pickZoneId)
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("관리자가 아닌 경우 픽존을 수정할 수 없다.")
    @Test
    void updatePickZone_Unauthorized() {
        Member member = memberTestHelper.createMember();
        String accessToken = memberTestHelper.getAccessTokenFromMember(member);

        PickZoneRequest pickZoneRequest = pickZoneTestHelper.createPickZoneRequest();
        long pickZoneId = pickZoneTestHelper.createPickZone(pickZoneRequest);

        PickZoneRequest updatePickZoneRequest = pickZoneTestHelper.createPickZoneRequest();

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(updatePickZoneRequest)
                .when().put("pick-zones/" + pickZoneId)
                .then().log().all()
                .statusCode(403);
    }

    @DisplayName("관리자인 경우 픽존을 삭제할 수 있다.")
    @Test
    void deletePickZone() {
        Member admin = memberTestHelper.createAdmin();
        String accessToken = memberTestHelper.getAccessTokenFromMember(admin);

        PickZoneRequest pickZoneRequest = pickZoneTestHelper.createPickZoneRequest();
        long pickZoneId = pickZoneTestHelper.createPickZone(pickZoneRequest);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .when().delete("pick-zones/" + pickZoneId)
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("관리자가 아닌 경우 픽존을 삭제할 수 없다.")
    @Test
    void deletePickZone_Unauthorized() {
        Member member = memberTestHelper.createMember();
        String accessToken = memberTestHelper.getAccessTokenFromMember(member);

        PickZoneRequest pickZoneRequest = pickZoneTestHelper.createPickZoneRequest();
        long pickZoneId = pickZoneTestHelper.createPickZone(pickZoneRequest);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .when().delete("pick-zones/" + pickZoneId)
                .then().log().all()
                .statusCode(403);
    }

    @DisplayName("존재하지 않는 픽존을 삭제할 수 없다.")
    @Test
    void deletePickZone_NotFound() {
        Member admin = memberTestHelper.createAdmin();
        String accessToken = memberTestHelper.getAccessTokenFromMember(admin);

        long notExistPickZoneId = 999L;

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .when().delete("pick-zones/" + notExistPickZoneId)
                .then().log().all()
                .statusCode(400);
    }
}
