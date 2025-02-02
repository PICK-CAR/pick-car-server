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
    private static final String VERIFIED_IDENTIFIER_KEY = "verified-id:";
    private static final Duration EXPIRATION = Duration.ofMinutes(10);

    private final RedisTemplate<String, String> verificationCodeTemplate;

    @Transactional
    @Override
    public void save(String identifier, String verificationCode) {
        String key = VERIFICATION_CODE_KEY + identifier;
        verificationCodeTemplate.opsForValue()
                .set(key, verificationCode, EXPIRATION);
    }

    @Transactional(readOnly = true)
    @Override
    public String findByIdentifier(String identifier) {
        String key = VERIFICATION_CODE_KEY + identifier;
        return verificationCodeTemplate.opsForValue().get(key);
    }

    @Transactional
    @Override
    public void completeVerification(String identifier) {
        String key = VERIFICATION_CODE_KEY + identifier;
        verificationCodeTemplate.delete(key);

        String verifiedKey = VERIFIED_IDENTIFIER_KEY + identifier;
        verificationCodeTemplate.opsForValue()
                .set(verifiedKey, identifier, EXPIRATION);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByVerifiedIdentifier(String identifier) {
        String key = VERIFIED_IDENTIFIER_KEY + identifier;
        return verificationCodeTemplate.hasKey(key);
    }
}
