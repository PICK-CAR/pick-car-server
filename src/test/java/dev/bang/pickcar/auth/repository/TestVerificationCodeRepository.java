package dev.bang.pickcar.auth.repository;

import static dev.bang.pickcar.member.MemberTestData.VALID_EMAIL;
import static dev.bang.pickcar.member.MemberTestData.VALID_PHONE_NUMBER;
import static dev.bang.pickcar.member.MemberTestData.VERIFICATION_CODE;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

/**
 * 테스트용 인증 코드 저장소입니다.
 * 테스트 데이터인 VALID_PHONE_NUMBER 를 사용하여 VERIFICATION_CODE 를 반환합니다.
 * @see dev.bang.pickcar.member.MemberTestData
 */
@Repository
@Profile("test")
public class TestVerificationCodeRepository implements VerificationCodeRepository {

    @Override
    public void save(String identifier, String verificationCode) {
    }

    @Override
    public String findByIdentifier(String identifier) {
        return VERIFICATION_CODE;
    }

    @Override
    public void completeVerification(String phoneNumber) {
    }

    @Override
    public boolean existsByVerifiedIdentifier(String identifier) {
        return VALID_PHONE_NUMBER.equals(identifier)
                || VALID_EMAIL.equals(identifier);
    }
}
