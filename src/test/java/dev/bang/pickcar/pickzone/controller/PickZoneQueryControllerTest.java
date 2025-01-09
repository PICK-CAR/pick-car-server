package dev.bang.pickcar.pickzone.controller;

import static dev.bang.pickcar.pickzone.PickZoneTestData.VALID_NAME;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

import dev.bang.pickcar.member.MemberTestHelper;
import dev.bang.pickcar.member.entity.Member;
import dev.bang.pickcar.pickzone.PickZoneTestHelper;
import dev.bang.pickcar.pickzone.dto.MapBounds;
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

@DisplayName("PickZoneQueryController 테스트")
@ActiveProfiles("test")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PickZoneQueryControllerTest {

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

    @DisplayName("주변 픽존을 조회할 수 있다.")
    @Test
    void getPickZonesNearby() {
        Member member = memberTestHelper.createMember();
        String accessToken = memberTestHelper.getAccessTokenFromMember(member);

        pickZoneTestHelper.createPickZone();
        MapBounds mapBounds = pickZoneTestHelper.createMapBounds();

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(mapBounds)
                .when().post("pick-zones/nearby")
                .then().log().all()
                .statusCode(200)
                .body("size()", equalTo(1));
    }

    @DisplayName("id로 픽존을 조회할 수 있다.")
    @Test
    void getPickZone() {
        Member admin = memberTestHelper.createAdmin();
        String accessToken = memberTestHelper.getAccessTokenFromMember(admin);

        PickZoneRequest pickZoneRequest = pickZoneTestHelper.createPickZoneRequest();
        long pickZoneId = pickZoneTestHelper.createPickZone(pickZoneRequest);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .when().get("pick-zones/" + pickZoneId)
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("픽존 목록을 조회할 수 있다.")
    @Test
    void getPickZones() {
        Member admin = memberTestHelper.createAdmin();
        String accessToken = memberTestHelper.getAccessTokenFromMember(admin);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .when().get("pick-zones")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("픽존 목록을 조회할 때 페이지와 사이즈를 지정할 수 있다.")
    @Test
    void getPickZones_PageAndSize() {
        Member admin = memberTestHelper.createAdmin();
        String accessToken = memberTestHelper.getAccessTokenFromMember(admin);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when().get("pick-zones")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("픽존 목록을 조회할 때 키워드를 지정할 수 있다.")
    @Test
    void getPickZones_Keyword() {
        Member admin = memberTestHelper.createAdmin();
        String accessToken = memberTestHelper.getAccessTokenFromMember(admin);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .queryParam("keyword", VALID_NAME)
                .when().get("pick-zones")
                .then().log().all()
                .statusCode(200);
    }
}
