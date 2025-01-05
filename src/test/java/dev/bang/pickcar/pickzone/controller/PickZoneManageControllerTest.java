package dev.bang.pickcar.pickzone.controller;

import static dev.bang.pickcar.pickzone.PickZoneTestData.VALID_LATITUDE;
import static dev.bang.pickcar.pickzone.PickZoneTestData.VALID_LONGITUDE;
import static dev.bang.pickcar.pickzone.PickZoneTestData.VALID_NAME;
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

    @DisplayName("관리자는 id로 픽존을 조회할 수 있다.")
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

    @DisplayName("관리자가 아닌 경우 id로 픽존을 조회할 수 없다.")
    @Test
    void getPickZone_Unauthorized() {
        Member member = memberTestHelper.createMember();
        String accessToken = memberTestHelper.getAccessTokenFromMember(member);

        PickZoneRequest pickZoneRequest = pickZoneTestHelper.createPickZoneRequest();
        long pickZoneId = pickZoneTestHelper.createPickZone(pickZoneRequest);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .when().get("pick-zones/" + pickZoneId)
                .then().log().all()
                .statusCode(403);
    }

    //@GetMapping
    //    @PreAuthorize("hasRole('ADMIN')")
    //    @Override
    //    public ResponseEntity<PickZoneResponses> getPickZones(@RequestParam(required = false) String keyword,
    //                                                          @RequestParam(required = false) Double latitude,
    //                                                          @RequestParam(required = false) Double longitude,
    //                                                          @RequestParam(required = false) Integer page,
    //                                                          @RequestParam(required = false) Integer size) {
    //        PickZoneSearchCondition searchCondition = new PickZoneSearchCondition(keyword, latitude, longitude, page, size);
    //        return ResponseEntity.ok(pickZoneManageService.getPickZones(searchCondition));
    //    }

    @DisplayName("관리자는 픽존 목록을 조회할 수 있다.")
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

    @DisplayName("관리자가 아닌 경우 픽존 목록을 조회할 수 없다.")
    @Test
    void getPickZones_Unauthorized() {
        Member member = memberTestHelper.createMember();
        String accessToken = memberTestHelper.getAccessTokenFromMember(member);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .when().get("pick-zones")
                .then().log().all()
                .statusCode(403);
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

    @DisplayName("픽존 목록을 조회할 때 위도와 경도를 지정할 수 있다.")
    @Test
    void getPickZones_LatitudeAndLongitude() {
        Member admin = memberTestHelper.createAdmin();
        String accessToken = memberTestHelper.getAccessTokenFromMember(admin);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .queryParam("latitude", VALID_LATITUDE)
                .queryParam("longitude", VALID_LONGITUDE)
                .when().get("pick-zones")
                .then().log().all()
                .statusCode(200);
    }
}
