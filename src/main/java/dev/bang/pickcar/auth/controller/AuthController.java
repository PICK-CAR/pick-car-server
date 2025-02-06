package dev.bang.pickcar.auth.controller;

import dev.bang.pickcar.auth.controller.docs.AuthApiDocs;
import dev.bang.pickcar.auth.controller.facade.AuthFacade;
import dev.bang.pickcar.auth.dto.EmailRequest;
import dev.bang.pickcar.auth.dto.EmailVerifyRequest;
import dev.bang.pickcar.auth.dto.LoginRequest;
import dev.bang.pickcar.auth.dto.PhoneNumberRequest;
import dev.bang.pickcar.auth.dto.TokenResponse;
import dev.bang.pickcar.auth.dto.VerificationCodeResponse;
import dev.bang.pickcar.member.dto.MemberRequest;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController implements AuthApiDocs {

    private final AuthFacade authFacade;

    @PostMapping("signup")
    @Override
    public ResponseEntity<Void> signup(@RequestBody @Valid MemberRequest memberRequest) {
        URI resourceUri = URI.create(authFacade.signup(memberRequest));
        return ResponseEntity.created(resourceUri).build();
    }

    @PostMapping("login")
    @Override
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        TokenResponse token = authFacade.login(loginRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("check/email")
    @Override
    public ResponseEntity<Boolean> checkEmailDuplication(@RequestBody @Valid EmailRequest req) {
        return ResponseEntity.ok(authFacade.checkEmailDuplication(req.email()));
    }

    @PostMapping("verification/email/send")
    @Override
    public ResponseEntity<Void> sendVerificationCodeToEmail(@RequestBody @Valid EmailRequest req) {
        authFacade.sendVerificationCodeToEmail(req.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("verification/email/verify")
    @Override
    public ResponseEntity<Void> verifyEmail(@RequestBody @Valid EmailVerifyRequest emailVerifyRequest) {
        authFacade.verifyEmail(emailVerifyRequest.email(), emailVerifyRequest.verificationCode());
        return ResponseEntity.ok().build();
    }

    @PostMapping("verification/phone/code")
    @Override
    public ResponseEntity<VerificationCodeResponse> issueVerificationCode(@RequestBody @Valid PhoneNumberRequest req) {
        VerificationCodeResponse verificationCode = authFacade.issueVerificationCode(req.phoneNumber());
        return ResponseEntity.ok(verificationCode);
    }

    @PostMapping("verification/phone/verify")
    @Override
    public ResponseEntity<Void> verifyVerificationNumber(@RequestBody @Valid PhoneNumberRequest req) {
        authFacade.verifyPhoneNumber(req.phoneNumber());
        return ResponseEntity.ok().build();
    }
}
