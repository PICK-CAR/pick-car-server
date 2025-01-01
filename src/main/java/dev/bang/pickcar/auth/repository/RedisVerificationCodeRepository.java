package dev.bang.pickcar.auth.repository;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Profile("!test")
@RequiredArgsConstructor
public class RedisVerificationCodeRepository implements VerificationCodeRepository {

    private static final String VERIFICATION_CODE_KEY = "verification-code:";
    private static final String VERIFIED_PHONE_NUMBER_KEY = "verified-phone:";
    private static final Duration EXPIRATION = Duration.ofMinutes(10);

    private final RedisTemplate<String, String> verificationCodeTemplate;

    @Transactional
    @Override
    public void save(String phoneNumber, String verificationCode) {
        String key = VERIFICATION_CODE_KEY + phoneNumber;
        verificationCodeTemplate.opsForValue()
                .set(key, verificationCode, EXPIRATION);
    }

    @Transactional(readOnly = true)
    @Override
    public String findByPhoneNumber(String phoneNumber) {
        String key = VERIFICATION_CODE_KEY + phoneNumber;
        return verificationCodeTemplate.opsForValue()
                .get(key);
    }

    @Transactional
    @Override
    public void completeVerification(String phoneNumber) {
        String key = VERIFICATION_CODE_KEY + phoneNumber;
        verificationCodeTemplate.delete(key);

        String verifiedKey = VERIFIED_PHONE_NUMBER_KEY + phoneNumber;
        verificationCodeTemplate.opsForValue()
                .set(verifiedKey, phoneNumber, EXPIRATION);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByVerifiedPhoneNumber(String phoneNumber) {
        String key = VERIFIED_PHONE_NUMBER_KEY + phoneNumber;
        return verificationCodeTemplate.hasKey(key);
    }
}
