package dev.bang.pickcar.auth.repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("local")
public class MemoryVerificationCodeRepository implements VerificationCodeRepository {

    private final Map<String, String> verificationCodeMap = new HashMap<>();
    private final Set<String> verifiedPhoneNumbers = new HashSet<>();

    @Override
    public void save(String phoneNumber, String verificationCode) {
        verificationCodeMap.put(phoneNumber, verificationCode);
    }

    @Override
    public String findByPhoneNumber(String phoneNumber) {
        return verificationCodeMap.get(phoneNumber);
    }

    @Override
    public void completeVerification(String phoneNumber) {
        verificationCodeMap.remove(phoneNumber);
        verifiedPhoneNumbers.add(phoneNumber);
    }

    @Override
    public boolean existsByVerifiedPhoneNumber(String phoneNumber) {
        return verifiedPhoneNumbers.contains(phoneNumber);
    }
}
