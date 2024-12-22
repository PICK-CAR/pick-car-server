package dev.bang.pickcar.auth.repository;

public interface VerificationCodeRepository {

    void save(String phoneNumber, String verificationCode);

    String findByPhoneNumber(String phoneNumber);

    void completeVerification(String phoneNumber);

    boolean existsByVerifiedPhoneNumber(String phoneNumber);
}
