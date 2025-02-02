package dev.bang.pickcar.auth.service;

import dev.bang.pickcar.auth.repository.VerificationCodeRepository;
import dev.bang.pickcar.auth.util.mail.MailReader;
import dev.bang.pickcar.auth.util.mail.MailSender;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private static final String VERIFICATION_CODE_EMAIL_SUBJECT = "PICKCAR 회원가입 인증번호";

    private final VerificationCodeRepository verificationCodeRepository;
    private final MailReader mailReader;
    private final MailSender mailSender;

    public String generateVerificationCode(String identifier) {
        String verificationCode = createUUID();
        verificationCodeRepository.save(identifier, verificationCode);
        return verificationCode;
    }

    private String createUUID() {
        return UUID.randomUUID()
                .toString();
    }

    public void processVerification(String identifier) {
        String message = mailReader.findLatestMessageBySenderPhoneNumber(identifier);
        String verificationCode = verificationCodeRepository.findByIdentifier(identifier);
        if (message.contains(verificationCode)) {
            verificationCodeRepository.completeVerification(identifier);
            return;
        }
        throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
    }

    public void checkVerifiedPhoneNumber(String phoneNumber) {
        if (!verificationCodeRepository.existsByVerifiedIdentifier(phoneNumber)) {
            throw new IllegalArgumentException("인증되지 않은 휴대폰 번호입니다.");
        }
    }

    public void sendVerificationCodeToEmail(String email) {
        String verificationCode = generateVerificationCode(email);
        mailSender.send(
                email,
                VERIFICATION_CODE_EMAIL_SUBJECT,
                "인증번호: " + verificationCode
        );
    }

    public boolean verifyEmail(String email, String verificationCode) {
        String savedVerificationCode = verificationCodeRepository.findByIdentifier(email);
        boolean isVerified = savedVerificationCode.equals(verificationCode);
        if (!isVerified) {
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
        }
        verificationCodeRepository.completeVerification(email);
        return true;
    }

    public void checkVerifiedEmail(String email) {
        if (!verificationCodeRepository.existsByVerifiedIdentifier(email)) {
            throw new IllegalArgumentException("인증되지 않은 이메일입니다.");
        }
    }
}
