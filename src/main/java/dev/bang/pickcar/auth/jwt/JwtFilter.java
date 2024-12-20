package dev.bang.pickcar.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN_TYPE = "Bearer";
    private static final String MEMBER_ROLE_PREFIX = "ROLE_";

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws IOException, ServletException {
        String token = resolveToken(request);
        if (StringUtils.hasText(token) && isValidateToken(token)) {
            setAuthentication(token);
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(AUTH_HEADER);
        if (StringUtils.hasText(token) && token.startsWith(TOKEN_TYPE)) {
            return parseToken(token);
        }
        return null;
    }

    private String parseToken(String token) {
        return token.substring(TOKEN_TYPE.length()).trim();
    }

    private boolean isValidateToken(String token) {
        return tokenProvider.validateToken(token);
    }

    private void setAuthentication(String token) {
        String memberId = tokenProvider.extractMemberIdFromToken(token);
        String memberRole = tokenProvider.extractMemberRoleFromToken(token);
        UsernamePasswordAuthenticationToken authentication = getAuthentication(memberId, memberRole);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String memberId, String memberRole) {
        var authorities = getAuthority(memberRole);
        return new UsernamePasswordAuthenticationToken(memberId, null, authorities);
    }

    private List<SimpleGrantedAuthority> getAuthority(String memberRole) {
        return List.of(new SimpleGrantedAuthority(MEMBER_ROLE_PREFIX + memberRole));
    }
}
