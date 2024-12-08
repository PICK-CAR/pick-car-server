package dev.bang.pickcar.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${security.whitelist}")
    private List<String> whitelist;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .headers(this::configureSecurityHeaders)
                .sessionManagement(this::configureSessionManagement)
                .authorizeHttpRequests(requests ->
                        requests
                                .requestMatchers(getWhitelistedMatchers()).permitAll()
                                .anyRequest().authenticated()
                )
                .build();
    }

    private RequestMatcher[] getWhitelistedMatchers() {
        return whitelist.stream()
                .map(AntPathRequestMatcher::new)
                .toArray(AntPathRequestMatcher[]::new);
    }

    private void configureSecurityHeaders(HeadersConfigurer<HttpSecurity> config) {
        config.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable);
    }

    private void configureSessionManagement(SessionManagementConfigurer<HttpSecurity> session) {
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
