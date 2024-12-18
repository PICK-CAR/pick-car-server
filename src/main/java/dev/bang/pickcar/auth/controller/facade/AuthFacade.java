package dev.bang.pickcar.auth.controller.facade;

import dev.bang.pickcar.auth.dto.LoginRequest;
import dev.bang.pickcar.auth.dto.MemberAuthResponse;
import dev.bang.pickcar.auth.dto.TokenResponse;
import dev.bang.pickcar.auth.service.AuthService;
import dev.bang.pickcar.member.dto.MemberRequest;
import dev.bang.pickcar.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthFacade {

    private static final String USER_RESOURCE_LOCATION = "users/";

    private final AuthService authService;
    private final MemberService memberService;

    public String signup(MemberRequest memberRequest) {
        String encryptedPassword = authService.encryptPassword(memberRequest.password());
        Long memberId = memberService.create(memberRequest, encryptedPassword);
        return USER_RESOURCE_LOCATION + memberId;
    }

    public TokenResponse login(LoginRequest loginRequest) {
        MemberAuthResponse authResponse = authService.getMemberAuthDetails(loginRequest.email(), loginRequest.password());
        return authService.issueToken(authResponse);
    }
}
