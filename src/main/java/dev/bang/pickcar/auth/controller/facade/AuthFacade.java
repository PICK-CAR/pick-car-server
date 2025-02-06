package dev.bang.pickcar.auth.controller.facade;

import dev.bang.pickcar.auth.dto.LoginRequest;
import dev.bang.pickcar.auth.dto.MemberAuthResponse;
import dev.bang.pickcar.auth.dto.TokenResponse;
import dev.bang.pickcar.auth.dto.VerificationCodeResponse;
import dev.bang.pickcar.auth.service.AuthService;
import dev.bang.pickcar.auth.service.VerificationService;
import dev.bang.pickcar.member.dto.MemberRequest;
import dev.bang.pickcar.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthFacade {

    private static final String MEMBER_RESOURCE_LOCATION = "members/";

    private final AuthService authService;
    private final MemberService memberService;
    private final VerificationService verificationService;

    public String signup(MemberRequest memberRequest) {
        verificationService.checkVerifiedPhoneNumber(memberRequest.phoneNumber());
        verificationService.checkVerifiedEmail(memberRequest.email());
        String encryptedPassword = authService.encryptPassword(memberRequest.password());
        Long memberId = memberService.create(memberRequest, encryptedPassword);
        return MEMBER_RESOURCE_LOCATION + memberId;
    }

    public TokenResponse login(LoginRequest loginRequest) {
        MemberAuthResponse authResponse = authService.getMemberAuthDetails(loginRequest.email(), loginRequest.password());
        return authService.issueToken(authResponse);
    }

    public boolean checkEmailDuplication(String email) {
        return memberService.existsByEmail(email);
    }

    public void sendVerificationCodeToEmail(String email) {
        verificationService.sendVerificationCodeToEmail(email);
    }

    public void verifyEmail(String email, String verificationCode) {
        verificationService.verifyEmail(email, verificationCode);
    }

    public VerificationCodeResponse issueVerificationCode(String identifier) {
        return new VerificationCodeResponse(verificationService.generateVerificationCode(identifier));
    }

    public void verifyPhoneNumber(String phoneNumber) {
        verificationService.processVerification(phoneNumber);
    }
}
