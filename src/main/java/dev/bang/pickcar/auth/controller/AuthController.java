package dev.bang.pickcar.auth.controller;

import dev.bang.pickcar.auth.controller.facade.AuthFacade;
import dev.bang.pickcar.auth.dto.LoginRequest;
import dev.bang.pickcar.auth.dto.TokenResponse;
import dev.bang.pickcar.member.dto.MemberRequest;
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
public class AuthController {

    private final AuthFacade authFacade;

    @PostMapping("signup")
    public ResponseEntity<Void> signup(@RequestBody MemberRequest memberRequest) {
        URI resourceUri = URI.create(authFacade.signup(memberRequest));
        return ResponseEntity.created(resourceUri)
                .build();
    }

    @PostMapping("login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        TokenResponse token = authFacade.login(loginRequest);
        return ResponseEntity.ok(token);
    }
}
