package dev.bang.pickcar.reservation;

import static dev.bang.pickcar.car.CarTestData.VALID_CAR_COLOR;
import static dev.bang.pickcar.car.CarTestData.VALID_CAR_FUEL_LEVEL;
import static dev.bang.pickcar.car.CarTestData.VALID_CAR_HOUR_RATE;
import static dev.bang.pickcar.car.CarTestData.VALID_CAR_MILEAGE;
import static dev.bang.pickcar.car.CarTestData.VALID_CAR_MODEL;
import static dev.bang.pickcar.car.CarTestData.VALID_CAR_NUMBER;
import static dev.bang.pickcar.car.CarTestData.VALID_CAR_REGISTRATION_DATE;
import static dev.bang.pickcar.car.CarTestData.VALID_CAR_VIN;
import static dev.bang.pickcar.member.MemberTestData.VALID_BIRTHDAY;
import static dev.bang.pickcar.member.MemberTestData.VALID_EMAIL;
import static dev.bang.pickcar.member.MemberTestData.VALID_NAME;
import static dev.bang.pickcar.member.MemberTestData.VALID_NICKNAME;
import static dev.bang.pickcar.member.MemberTestData.VALID_PASSWORD;
import static dev.bang.pickcar.member.MemberTestData.VALID_PHONE_NUMBER;

import dev.bang.pickcar.car.entity.Car;
import dev.bang.pickcar.member.entity.Member;
import dev.bang.pickcar.reservation.entity.Reservation;
import java.time.LocalDateTime;

public class ReservationTestData {

    public static final Member VALID_MEMBER = new Member(
            VALID_NAME,
            VALID_NICKNAME,
            VALID_EMAIL,
            VALID_PASSWORD,
            VALID_BIRTHDAY,
            VALID_PHONE_NUMBER
    );
    public static final Car VALID_CAR = Car.builder()
            .model(VALID_CAR_MODEL)
            .color(VALID_CAR_COLOR)
            .vin(VALID_CAR_VIN)
            .licensePlate(VALID_CAR_NUMBER)
            .registrationDate(VALID_CAR_REGISTRATION_DATE)
            .mileage(VALID_CAR_MILEAGE)
            .fuelLevel(VALID_CAR_FUEL_LEVEL)
            .hourlyRate(VALID_CAR_HOUR_RATE)
            .build();
    public static final LocalDateTime VALID_START_DATE = LocalDateTime.now().plusDays(1);
    public static final LocalDateTime VALID_END_DATE = LocalDateTime.now().plusDays(2);
    public static final Reservation VALID_RESERVATION = Reservation.builder()
            .member(VALID_MEMBER)
            .car(VALID_CAR)
            .startDateTime(VALID_START_DATE)
            .endDateTime(VALID_END_DATE)
            .build();

    private ReservationTestData() {
    }
}
