package dev.bang.pickcar.reservation;

import static dev.bang.pickcar.reservation.ReservationTestData.VALID_END_DATE;
import static dev.bang.pickcar.reservation.ReservationTestData.VALID_MEMBER;
import static dev.bang.pickcar.reservation.ReservationTestData.VALID_START_DATE;

import dev.bang.pickcar.car.CarTestHelper;
import dev.bang.pickcar.car.dto.CarRequest;
import dev.bang.pickcar.car.entity.Car;
import dev.bang.pickcar.car.entity.CarModel;
import dev.bang.pickcar.member.MemberTestHelper;
import dev.bang.pickcar.member.entity.Member;
import dev.bang.pickcar.member.repository.MemberRepository;
import dev.bang.pickcar.pickzone.PickZoneTestHelper;
import dev.bang.pickcar.reservation.dto.RequestReservation;
import dev.bang.pickcar.reservation.entity.Reservation;
import dev.bang.pickcar.reservation.repository.ReservationRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Component
@ActiveProfiles("test")
public class ReservationTestHelper {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CarTestHelper carTestHelper;

    @Autowired
    private PickZoneTestHelper pickZoneTestHelper;

    @Autowired
    private MemberTestHelper memberTestHelper;

    public RequestReservation createRequestReservation(Long carId) {
        return new RequestReservation(
                carId,
                VALID_START_DATE,
                VALID_END_DATE
        );
    }

    public RequestReservation createRequestReservation(Object... args) {
        return new RequestReservation(
                (Long) args[0],
                (LocalDateTime) args[1],
                (LocalDateTime) args[2]
        );
    }

    @Transactional
    public Car createBatchedCar() {
        Car car = createTestCar();
        car.assignPickZone(pickZoneTestHelper.createPickZone());
        return car;
    }

    @Transactional
    public Car createUnbatchedCar() {
        return createTestCar();
    }

    private Car createTestCar() {
        CarModel carModel = carTestHelper.createCarModel();
        CarRequest carRequest = carTestHelper.createCarRequest(carModel.getId());
        return carTestHelper.createCar(carRequest);
    }

    @Transactional
    public Reservation createReservation(Member member, Car car) {
        return reservationRepository.save(Reservation.builder()
                .member(member)
                .car(car)
                .startDateTime(VALID_START_DATE)
                .endDateTime(VALID_END_DATE)
                .build());
    }
}
