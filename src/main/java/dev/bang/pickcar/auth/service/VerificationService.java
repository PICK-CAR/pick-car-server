package dev.bang.pickcar.auth.service;

import dev.bang.pickcar.auth.repository.VerificationCodeRepository;
import dev.bang.pickcar.auth.util.MailReader;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final VerificationCodeRepository verificationCodeRepository;
    private final MailReader mailReader;

    public String generateVerificationCode(String phoneNumber) {
        String verificationCode = createUUID();
        verificationCodeRepository.save(phoneNumber, verificationCode);
        return verificationCode;
    }

    private String createUUID() {
        return UUID.randomUUID()
                .toString();
    }

    public void processVerification(String phoneNumber) {
        String message = mailReader.findLatestMessageBySenderPhoneNumber(phoneNumber);
        String verificationCode = verificationCodeRepository.findByPhoneNumber(phoneNumber);
        if (message.contains(verificationCode)) {
            verificationCodeRepository.completeVerification(phoneNumber);
            return;
        }
        throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
    }

    public void checkVerifiedPhoneNumber(String phoneNumber) {
        if (!verificationCodeRepository.existsByVerifiedPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("인증되지 않은 휴대폰 번호입니다.");
        }
    }
}
