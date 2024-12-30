package dev.bang.pickcar.car.controller;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

import dev.bang.pickcar.car.CarTestHelper;
import dev.bang.pickcar.car.dto.CarModelRequest;
import dev.bang.pickcar.member.MemberTestHelper;
import dev.bang.pickcar.member.entity.Member;
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

@DisplayName("CarModelController 테스트")
@ActiveProfiles("test")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CarModelManageControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MemberTestHelper memberTestHelper;

    @Autowired
    private CarTestHelper carTestHelper;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("관리자인 경우 새로운 차량 모델을 등록할 수 있다.")
    @Test
    void createCarModel() {
        Member admin = memberTestHelper.createAdmin();
        String accessToken = memberTestHelper.getAccessTokenFromMember(admin);

        CarModelRequest carModelRequest = carTestHelper.createCarModelRequest();

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(carModelRequest)
                .when().post("models")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("관리자가 아닌 경우 새로운 차량 모델을 등록할 수 없다.")
    @Test
    void createCarModel_Unauthorized() {
        Member member = memberTestHelper.createMember();
        String accessToken = memberTestHelper.getAccessTokenFromMember(member);

        CarModelRequest carModelRequest = carTestHelper.createCarModelRequest();

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(carModelRequest)
                .when().post("models")
                .then().log().all()
                .statusCode(403);
    }

    @DisplayName("같은 차량 모델이 이미 존재하는 경우 새로운 차량 모델을 등록할 수 없다.")
    @Test
    void createCarModel_Duplicate() {
        Member admin = memberTestHelper.createAdmin();
        String accessToken = memberTestHelper.getAccessTokenFromMember(admin);

        CarModelRequest carModelRequest = carTestHelper.createCarModelRequest();
        carTestHelper.createCarModel(carModelRequest);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(carModelRequest)
                .when().post("models")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("관리자인 경우 차량 모델을 조회할 수 있다.")
    @Test
    void getCarModels() {
        Member admin = memberTestHelper.createAdmin();
        String accessToken = memberTestHelper.getAccessTokenFromMember(admin);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .when().get("models")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("존재하지 않는 차량 모델을 조회할 수 없다.")
    @Test
    void getCarModel_NotExist() {
        Member admin = memberTestHelper.createAdmin();
        String accessToken = memberTestHelper.getAccessTokenFromMember(admin);

        long notExistCarModelId = 0L;

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .when().get("models/" + notExistCarModelId)
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("관리자가 아닌 경우 차량 모델을 조회할 수 없다.")
    @Test
    void getCarModels_Unauthorized() {
        Member member = memberTestHelper.createMember();
        String accessToken = memberTestHelper.getAccessTokenFromMember(member);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .when().get("models")
                .then().log().all()
                .statusCode(403);
    }

    @DisplayName("관리자인 경우 특정 차량 모델을 조회할 수 있다.")
    @Test
    void getCarModel() {
        Member admin = memberTestHelper.createAdmin();
        String accessToken = memberTestHelper.getAccessTokenFromMember(admin);

        Long carModelId = carTestHelper.createCarModel().getId();

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .when().get("models/" + carModelId)
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("관리자가 아닌 경우 특정 차량 모델을 조회할 수 없다.")
    @Test
    void getCarModel_Unauthorized() {
        Member member = memberTestHelper.createMember();
        String accessToken = memberTestHelper.getAccessTokenFromMember(member);

        Long carModelId = carTestHelper.createCarModel().getId();

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .when().get("models/" + carModelId)
                .then().log().all()
                .statusCode(403);
    }

    @DisplayName("차량 모델을 삭제한 경우 차량 모델을 조회할 수 없다.")
    @Test
    void getCarModel_NotFound() {
        Member admin = memberTestHelper.createAdmin();
        String accessToken = memberTestHelper.getAccessTokenFromMember(admin);

        Long carModelId = carTestHelper.createCarModel().getId();

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .when().delete("models/" + carModelId)
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .when().get("models/" + carModelId)
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("관리자인 경우 특정 차량 모델을 삭제할 수 있다.")
    @Test
    void deleteCarModel() {
        Member admin = memberTestHelper.createAdmin();
        String accessToken = memberTestHelper.getAccessTokenFromMember(admin);

        Long carModelId = carTestHelper.createCarModel().getId();

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .when().delete("models/" + carModelId)
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("관리자가 아닌 경우 특정 차량 모델을 삭제할 수 없다.")
    @Test
    void deleteCarModel_Unauthorized() {
        Member member = memberTestHelper.createMember();
        String accessToken = memberTestHelper.getAccessTokenFromMember(member);

        Long carModelId = carTestHelper.createCarModel().getId();

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .when().delete("models/" + carModelId)
                .then().log().all()
                .statusCode(403);
    }

    @DisplayName("존재하지 않는 차량 모델을 삭제할 수 없다.")
    @Test
    void deleteCarModel_NotExist() {
        Member admin = memberTestHelper.createAdmin();
        String accessToken = memberTestHelper.getAccessTokenFromMember(admin);

        long notExistCarModelId = 0L;

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .when().delete("models/" + notExistCarModelId)
                .then().log().all()
                .statusCode(400);
    }
}
