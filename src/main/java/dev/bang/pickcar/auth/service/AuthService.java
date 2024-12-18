package dev.bang.pickcar.auth.service;

import dev.bang.pickcar.auth.dto.MemberAuthResponse;
import dev.bang.pickcar.auth.dto.TokenResponse;
import dev.bang.pickcar.auth.jwt.TokenProvider;
import dev.bang.pickcar.member.entity.Member;
import dev.bang.pickcar.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public TokenResponse issueToken(MemberAuthResponse authResponse) {
        return tokenProvider.generateToken(authResponse);
    }

    @Transactional(readOnly = true)
    public MemberAuthResponse getMemberAuthDetails(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("로그인 정보가 올바르지 않습니다."));
        if (!matchesPassword(password, member.getPassword())) {
            throw new IllegalArgumentException("로그인 정보가 올바르지 않습니다.");
        }
        return new MemberAuthResponse(member.getId(), member.getRole().name());
    }

    private boolean matchesPassword(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }
}
