package dev.bang.pickcar.reservation.controller;

import static dev.bang.pickcar.payment.PaymentTestData.ORDER_ID;
import static dev.bang.pickcar.payment.PaymentTestData.PAYMENT_KEY;
import static dev.bang.pickcar.reservation.ReservationTestData.VALID_START_DATE;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

import dev.bang.pickcar.car.entity.Car;
import dev.bang.pickcar.member.MemberTestHelper;
import dev.bang.pickcar.member.entity.Member;
import dev.bang.pickcar.payment.dto.RequestPaymentCancel;
import dev.bang.pickcar.payment.dto.RequestPaymentConfirm;
import dev.bang.pickcar.reservation.ReservationTestHelper;
import dev.bang.pickcar.reservation.dto.RequestReservation;
import dev.bang.pickcar.reservation.entity.Reservation;
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

@DisplayName("ReservationController 테스트")
@ActiveProfiles("test")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ReservationControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ReservationTestHelper reservationTestHelper;

    @Autowired
    private MemberTestHelper memberTestHelper;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("회원은 예약 가능한 공유 차량을 예약할 수 있다.")
    @Test
    void createReservation() {
        Member member = memberTestHelper.createMember();
        String accessToken = memberTestHelper.getAccessTokenFromMember(member);

        Car batchedCar = reservationTestHelper.createBatchedCar();
        RequestReservation requestReservation = reservationTestHelper.createRequestReservation(batchedCar.getId());

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(requestReservation)
                .when().post("reservations")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("회원은 예약 불가능한 공유 차량을 예약할 수 없다.")
    @Test
    void createReservationWithUnavailableCar() {
        Member member = memberTestHelper.createMember();
        String accessToken = memberTestHelper.getAccessTokenFromMember(member);

        // 픽존에 배치되지 않은 차량은 예약 불가능
        Car unbatchedCar = reservationTestHelper.createUnbatchedCar();
        RequestReservation requestReservation = reservationTestHelper.createRequestReservation(unbatchedCar.getId());

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(requestReservation)
                .when().post("reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("회원은 잘못된 예약 시간으로 예약할 수 없다.")
    @Test
    void createReservationWithInvalidTime() {
        Member member = memberTestHelper.createMember();
        String accessToken = memberTestHelper.getAccessTokenFromMember(member);

        Car batchedCar = reservationTestHelper.createBatchedCar();
        RequestReservation requestReservation = reservationTestHelper.createRequestReservation(
                batchedCar.getId(),
                VALID_START_DATE,
                VALID_START_DATE.minusDays(1)
        );

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(requestReservation)
                .when().post("reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("회원은 이미 예약된 시간대에 예약할 수 없다.")
    @Test
    void createReservationWithOverlappingTime() {
        Member member = memberTestHelper.createMember();
        String accessToken = memberTestHelper.getAccessTokenFromMember(member);

        Car batchedCar = reservationTestHelper.createBatchedCar();
        RequestReservation requestReservation = reservationTestHelper.createRequestReservation(batchedCar.getId());

        // 같은 시간으로 예약 생성
        reservationTestHelper.createReservation(member, batchedCar);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(requestReservation)
                .when().post("reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("회원은 이미 예약된 시간대라도 취소된 차량은 예약할 수 있다.")
    @Test
    void createReservationWithCanceledCar() {
        Member member = memberTestHelper.createMember();
        String accessToken = memberTestHelper.getAccessTokenFromMember(member);

        Car batchedCar = reservationTestHelper.createBatchedCar();
        RequestReservation requestReservation = reservationTestHelper.createRequestReservation(batchedCar.getId());

        // 예약 생성 후 취소
        Reservation reservation = reservationTestHelper.createReservation(member, batchedCar);
        callCancelReservationApi(reservation, accessToken);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(requestReservation)
                .when().post("reservations")
                .then().log().all()
                .statusCode(201);
    }

    private void callCancelReservationApi(Reservation reservation, String accessToken) {
        RequestPaymentConfirm requestPaymentConfirm = new RequestPaymentConfirm(
                ORDER_ID,
                reservation.calculateTotalPrice(),
                PAYMENT_KEY
        );

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(requestPaymentConfirm)
                .when().post("reservations/" + reservation.getId() + "/payment")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(new RequestPaymentCancel("단순 변심"))
                .when().post("reservations/" + reservation.getId() + "/cancel")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("회원은 이미 예약된 시간대라도 반납된 차량은 예약할 수 있다.")
    @Test
    void createReservationWithReturnedCar() {
        Member member = memberTestHelper.createMember();
        String accessToken = memberTestHelper.getAccessTokenFromMember(member);

        Car batchedCar = reservationTestHelper.createBatchedCar();
        RequestReservation requestReservation = reservationTestHelper.createRequestReservation(batchedCar.getId());

        // 예약 생성 후 반납
        Reservation reservation = reservationTestHelper.createReservation(member, batchedCar);
        callReturnReservationApi(reservation, accessToken);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(requestReservation)
                .when().post("reservations")
                .then().log().all()
                .statusCode(201);
    }

    private void callReturnReservationApi(Reservation reservation, String accessToken) {
        RequestPaymentConfirm requestPaymentConfirm = new RequestPaymentConfirm(
                ORDER_ID,
                reservation.calculateTotalPrice(),
                PAYMENT_KEY
        );

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(requestPaymentConfirm)
                .when().post("reservations/" + reservation.getId() + "/payment")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .when().post("reservations/" + reservation.getId() + "/return")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("회원은 본인의 예약을 결제할 수 있다.")
    @Test
    void payReservation() {
        Member member = memberTestHelper.createMember();
        String accessToken = memberTestHelper.getAccessTokenFromMember(member);

        Car batchedCar = reservationTestHelper.createBatchedCar();
        Reservation reservation = reservationTestHelper.createReservation(member, batchedCar);
        RequestPaymentConfirm requestPaymentConfirm = new RequestPaymentConfirm(
                ORDER_ID,
                reservation.calculateTotalPrice(),
                PAYMENT_KEY
        );

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(requestPaymentConfirm)
                .when().post("reservations/" + reservation.getId() + "/payment")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("본인의 예약만 결제할 수 있다.")
    @Test
    void payReservationWithOtherMember() {
        Member member = memberTestHelper.createMember();
        String accessToken = memberTestHelper.getAccessTokenFromMember(member);

        Car batchedCar = reservationTestHelper.createBatchedCar();
        Reservation reservation = reservationTestHelper.createReservation(
                memberTestHelper.createMember(), // 다른 회원의 예약
                batchedCar
        );
        RequestPaymentConfirm requestPaymentConfirm = new RequestPaymentConfirm(
                ORDER_ID,
                reservation.calculateTotalPrice(),
                PAYMENT_KEY
        );

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(requestPaymentConfirm)
                .when().post("reservations/" + reservation.getId() + "/payment")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("회원은 결제된 본인의 예약을 취소할 수 있다.")
    @Test
    void cancelReservation() {
        Member member = memberTestHelper.createMember();
        String accessToken = memberTestHelper.getAccessTokenFromMember(member);

        Car batchedCar = reservationTestHelper.createBatchedCar();
        Reservation reservation = reservationTestHelper.createReservation(member, batchedCar);

        RequestPaymentConfirm requestPaymentConfirm = new RequestPaymentConfirm(
                ORDER_ID,
                reservation.calculateTotalPrice(),
                PAYMENT_KEY
        );

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(requestPaymentConfirm)
                .when().post("reservations/" + reservation.getId() + "/payment")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(new RequestPaymentCancel("단순 변심"))
                .when().post("reservations/" + reservation.getId() + "/cancel")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("본인의 예약만 취소할 수 있다.")
    @Test
    void cancelReservationWithOtherMember() {
        Member member = memberTestHelper.createMember();
        String accessToken = memberTestHelper.getAccessTokenFromMember(member);

        Car batchedCar = reservationTestHelper.createBatchedCar();
        Reservation reservation = reservationTestHelper.createReservation(member, batchedCar);

        RequestPaymentConfirm requestPaymentConfirm = new RequestPaymentConfirm(
                ORDER_ID,
                reservation.calculateTotalPrice(),
                PAYMENT_KEY
        );

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(requestPaymentConfirm)
                .when().post("reservations/" + reservation.getId() + "/payment")
                .then().log().all()
                .statusCode(200);

        Member anotherMember = memberTestHelper.createMember();
        String anotherMemberAccessToken = memberTestHelper.getAccessTokenFromMember(anotherMember);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + anotherMemberAccessToken)
                .contentType(ContentType.JSON)
                .body(new RequestPaymentCancel("단순 변심"))
                .when().post("reservations/" + reservation.getId() + "/cancel")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("회원은 본인의 예약을 반납할 수 있다.")
    @Test
    void completeReturn() {
        Member member = memberTestHelper.createMember();
        String accessToken = memberTestHelper.getAccessTokenFromMember(member);

        Car batchedCar = reservationTestHelper.createBatchedCar();
        Reservation reservation = reservationTestHelper.createReservation(member, batchedCar);

        RequestPaymentConfirm requestPaymentConfirm = new RequestPaymentConfirm(
                ORDER_ID,
                reservation.calculateTotalPrice(),
                PAYMENT_KEY
        );

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(requestPaymentConfirm)
                .when().post("reservations/" + reservation.getId() + "/payment")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .when().post("reservations/" + reservation.getId() + "/return")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("본인의 예약만 반납할 수 있다.")
    @Test
    void completeReturnWithOtherMember() {
        Member member = memberTestHelper.createMember();
        String accessToken = memberTestHelper.getAccessTokenFromMember(member);

        Car batchedCar = reservationTestHelper.createBatchedCar();
        Reservation reservation = reservationTestHelper.createReservation(member, batchedCar);

        RequestPaymentConfirm requestPaymentConfirm = new RequestPaymentConfirm(
                ORDER_ID,
                reservation.calculateTotalPrice(),
                PAYMENT_KEY
        );

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(requestPaymentConfirm)
                .when().post("reservations/" + reservation.getId() + "/payment")
                .then().log().all()
                .statusCode(200);

        Member anotherMember = memberTestHelper.createMember();
        String anotherMemberAccessToken = memberTestHelper.getAccessTokenFromMember(anotherMember);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + anotherMemberAccessToken)
                .when().post("reservations/" + reservation.getId() + "/return")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("확정되지 않은 예약은 반납할 수 없다.")
    @Test
    void completeReturnWithUnconfirmedReservation() {
        Member member = memberTestHelper.createMember();
        String accessToken = memberTestHelper.getAccessTokenFromMember(member);

        Car batchedCar = reservationTestHelper.createBatchedCar();
        Reservation reservation = reservationTestHelper.createReservation(member, batchedCar);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .when().post("reservations/" + reservation.getId() + "/return")
                .then().log().all()
                .statusCode(400);
    }
}
