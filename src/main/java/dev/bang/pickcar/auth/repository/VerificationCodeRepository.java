package dev.bang.pickcar.auth.repository;

public interface VerificationCodeRepository {

    void save(String phoneNumber, String verificationCode);

    String findByIdentifier(String identifier);

    void completeVerification(String phoneNumber);

    boolean existsByVerifiedIdentifier(String identifier);
}
