package dev.bang.pickcar.car.controller;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

import dev.bang.pickcar.car.CarTestHelper;
import dev.bang.pickcar.car.dto.CarRequest;
import dev.bang.pickcar.car.dto.PickZoneAssignRequest;
import dev.bang.pickcar.car.entity.Car;
import dev.bang.pickcar.car.entity.CarModel;
import dev.bang.pickcar.member.MemberTestHelper;
import dev.bang.pickcar.member.entity.Member;
import dev.bang.pickcar.pickzone.PickZoneTestHelper;
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

@DisplayName("CarManageController 테스트")
@ActiveProfiles("test")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CarManageControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MemberTestHelper memberTestHelper;

    @Autowired
    private CarTestHelper carTestHelper;

    @Autowired
    private PickZoneTestHelper pickZoneTestHelper;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("관리자인 경우 새로운 차량을 등록할 수 있다.")
    @Test
    void createCar() {
        Member admin = memberTestHelper.createAdmin();
        String accessToken = memberTestHelper.getAccessTokenFromMember(admin);

        CarModel carModel = carTestHelper.createCarModel();
        CarRequest carRequest = carTestHelper.createCarRequest(carModel.getId());

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(carRequest)
                .when().post("cars")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("관리자가 아닌 경우 새로운 차량을 등록할 수 없다.")
    @Test
    void createCar_Unauthorized() {
        Member member = memberTestHelper.createMember();
        String accessToken = memberTestHelper.getAccessTokenFromMember(member);

        CarModel carModel = carTestHelper.createCarModel();
        CarRequest carRequest = carTestHelper.createCarRequest(carModel.getId());

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(carRequest)
                .when().post("cars")
                .then().log().all()
                .statusCode(403);
    }

    @DisplayName("같은 차량이 이미 존재하는 경우 새로운 차량을 등록할 수 없다.")
    @Test
    void createCar_Duplicate() {
        Member admin = memberTestHelper.createAdmin();
        String accessToken = memberTestHelper.getAccessTokenFromMember(admin);

        CarModel carModel = carTestHelper.createCarModel();
        CarRequest carRequest = carTestHelper.createCarRequest(carModel.getId());
        carTestHelper.createCar(carRequest);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(carRequest)
                .when().post("cars")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("관리자인 경우 차량을 PickZone에 배치할 수 있다.")
    @Test
    void assignCarToPickZone() {
        Member admin = memberTestHelper.createAdmin();
        String accessToken = memberTestHelper.getAccessTokenFromMember(admin);

        CarModel carModel = carTestHelper.createCarModel();
        CarRequest carRequest = carTestHelper.createCarRequest(carModel.getId());
        Car car = carTestHelper.createCar(carRequest);

        long pickZoneId = pickZoneTestHelper.createPickZone();
        PickZoneAssignRequest pickZoneAssignRequest = new PickZoneAssignRequest(pickZoneId);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(pickZoneAssignRequest)
                .when().post("cars/" + car.getId() + "/pick-zones")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("관리자가 아닌 경우 차량을 PickZone에 배치할 수 없다.")
    @Test
    void assignCarToPickZone_Unauthorized() {
        Member member = memberTestHelper.createMember();
        String accessToken = memberTestHelper.getAccessTokenFromMember(member);

        CarModel carModel = carTestHelper.createCarModel();
        CarRequest carRequest = carTestHelper.createCarRequest(carModel.getId());
        Car car = carTestHelper.createCar(carRequest);

        long pickZoneId = pickZoneTestHelper.createPickZone();
        PickZoneAssignRequest pickZoneAssignRequest = new PickZoneAssignRequest(pickZoneId);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(pickZoneAssignRequest)
                .when().post("cars/" + car.getId() + "/pick-zones")
                .then().log().all()
                .statusCode(403);
    }

    @DisplayName("존재하지 않는 차량을 PickZone에 배치할 수 없다.")
    @Test
    void assignCarToPickZone_NotFound() {
        Member admin = memberTestHelper.createAdmin();
        String accessToken = memberTestHelper.getAccessTokenFromMember(admin);

        long notExistCarId = 999L;
        long pickZoneId = pickZoneTestHelper.createPickZone();
        PickZoneAssignRequest pickZoneAssignRequest = new PickZoneAssignRequest(pickZoneId);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(pickZoneAssignRequest)
                .when().post("cars/" + notExistCarId + "/pick-zones")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("존재하지 않는 PickZone에 차량을 배치할 수 없다.")
    @Test
    void assignCarToPickZone_PickZoneNotFound() {
        Member admin = memberTestHelper.createAdmin();
        String accessToken = memberTestHelper.getAccessTokenFromMember(admin);

        CarModel carModel = carTestHelper.createCarModel();
        CarRequest carRequest = carTestHelper.createCarRequest(carModel.getId());
        Car car = carTestHelper.createCar(carRequest);

        long notExistPickZoneId = 999L;
        PickZoneAssignRequest pickZoneAssignRequest = new PickZoneAssignRequest(notExistPickZoneId);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(pickZoneAssignRequest)
                .when().post("cars/" + car.getId() + "/pick-zones")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("관리자인 경우 차량을 삭제할 수 있다.")
    @Test
    void deleteCar() {
        Member admin = memberTestHelper.createAdmin();
        String accessToken = memberTestHelper.getAccessTokenFromMember(admin);

        CarModel carModel = carTestHelper.createCarModel();
        CarRequest carRequest = carTestHelper.createCarRequest(carModel.getId());
        Car car = carTestHelper.createCar(carRequest);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .when().delete("cars/" + car.getId())
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("관리자가 아닌 경우 차량을 삭제할 수 없다.")
    @Test
    void deleteCar_Unauthorized() {
        Member member = memberTestHelper.createMember();
        String accessToken = memberTestHelper.getAccessTokenFromMember(member);

        CarModel carModel = carTestHelper.createCarModel();
        CarRequest carRequest = carTestHelper.createCarRequest(carModel.getId());
        Car car = carTestHelper.createCar(carRequest);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .when().delete("cars/" + car.getId())
                .then().log().all()
                .statusCode(403);
    }

    @DisplayName("존재하지 않는 차량을 삭제할 수 없다.")
    @Test
    void deleteCar_NotFound() {
        Member admin = memberTestHelper.createAdmin();
        String accessToken = memberTestHelper.getAccessTokenFromMember(admin);

        long notExistCarId = 999L;

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .when().delete("cars/" + notExistCarId)
                .then().log().all()
                .statusCode(400);
    }
}
