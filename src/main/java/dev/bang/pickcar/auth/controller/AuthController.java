package dev.bang.pickcar.auth.controller;

import dev.bang.pickcar.auth.controller.docs.AuthApiDocs;
import dev.bang.pickcar.auth.controller.facade.AuthFacade;
import dev.bang.pickcar.auth.dto.LoginRequest;
import dev.bang.pickcar.auth.dto.TokenResponse;
import dev.bang.pickcar.member.dto.MemberRequest;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        return ResponseEntity.created(resourceUri)
                .build();
    }

    @PostMapping("login")
    @Override
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        TokenResponse token = authFacade.login(loginRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("verification/issue")
    @Override
    public ResponseEntity<String> issueVerificationCode(@RequestParam String phoneNumber) {
        String verificationCode = authFacade.issueVerificationCode(phoneNumber);
        return ResponseEntity.ok(verificationCode);
    }

    @PostMapping("verification/verify")
    @Override
    public ResponseEntity<Void> verifyVerificationNumber(@RequestParam String phoneNumber) {
        authFacade.verifyPhoneNumber(phoneNumber);
        return ResponseEntity.ok()
                .build();
    }
}
