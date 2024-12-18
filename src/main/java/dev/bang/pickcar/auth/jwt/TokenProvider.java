package dev.bang.pickcar.auth.jwt;

import dev.bang.pickcar.auth.dto.MemberAuthResponse;
import dev.bang.pickcar.auth.dto.TokenResponse;
import dev.bang.pickcar.config.SecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final String MEMBER_ID_KEY = "memberId";
    private static final String MEMBER_ROLE_KEY = "memberRole";

    private final SecurityProperties securityProperties;

    public TokenResponse generateToken(MemberAuthResponse authResponse) {
        String accessToken = buildJwtToken(authResponse.memberId(), authResponse.role());
        return new TokenResponse(accessToken);
    }

    private String buildJwtToken(Long memberId, String memberRole) {
        Date validity = calculateExpiration(getExpirationTime());
        return Jwts.builder()
                .subject(memberId.toString())
                .claim(MEMBER_ID_KEY, memberId)
                .claim(MEMBER_ROLE_KEY, memberRole)
                .expiration(validity)
                .signWith(Keys.hmacShaKeyFor(getSecretKey().getBytes()))
                .compact();
    }

    private Date calculateExpiration(long expiration) {
        Date now = new Date();
        return new Date(now.getTime() + expiration);
    }

    public String extractMemberIdFromToken(String token) {
        return extractClaimFromToken(token, MEMBER_ID_KEY);
    }

    public String extractMemberRoleFromToken(String token) {
        return extractClaimFromToken(token, MEMBER_ROLE_KEY);
    }

    public String extractClaimFromToken(String token, String claimKey) {
        try {
            return decodeToken(token)
                    .get(claimKey)
                    .toString();
        } catch (ExpiredJwtException exception) {
            throw new IllegalArgumentException("만료된 토큰입니다.", exception.getCause());
        } catch (Exception exception) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.", exception.getCause());
        }
    }

    private Claims decodeToken(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(getSecretKey().getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        try {
            decodeToken(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    private String getSecretKey() {
        return securityProperties.jwt().secretKey();
    }

    private long getExpirationTime() {
        return securityProperties.jwt().expirationTime();
    }
}
