package dev.bang.pickcar.global.config;

import dev.bang.pickcar.global.config.properties.SwaggerProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerConfig {

    private static final String DOCUMENTS_TITLE = "Pick-Car API Documents";
    private static final String DOCUMENTS_VERSION = "0.0.1";
    private static final String DOCUMENTS_DESCRIPTION = "Pick-Car API 명세서입니다.";
    private static final String SERVER_NAME = "Pick-Car";
    private static final String GITHUB_URL = "https://github.com/PICK-CAR/pick-car-server";
    private static final String AUTH_NAME = "Authorization";

    private final Environment environment;
    private final SwaggerProperties swaggerProperties;

    @Bean
    public OpenAPI createOpenApi() {
        return new OpenAPI()
                .info(getInfo())
                .servers(getServers())
                .addSecurityItem(getSecurityRequirement())
                .components(getComponents());
    }

    private Info getInfo() {
        return new Info()
                .title(DOCUMENTS_TITLE)
                .version("v" + DOCUMENTS_VERSION)
                .description(DOCUMENTS_DESCRIPTION)
                .license(getLicense());
    }

    private License getLicense() {
        return new License()
                .name(SERVER_NAME)
                .url(GITHUB_URL);
    }

    private List<Server> getServers() {
        return List.of(getServerByProfile());
    }

    private Server getServerByProfile() {
        if (environment.getActiveProfiles().length == 0) {
            return new Server()
                    .url(swaggerProperties.localServerUrl())
                    .description("Local Server");
        }
        if (environment.getActiveProfiles()[0].equals("dev")) {
            return new Server()
                    .url(swaggerProperties.devServerUrl())
                    .description("Dev Server");
        }
        return new Server()
                .url(swaggerProperties.localServerUrl())
                .description("Local Server");
    }

    private SecurityRequirement getSecurityRequirement() {
        return new SecurityRequirement()
                .addList(AUTH_NAME);
    }

    private Components getComponents() {
        return new Components()
                .addSecuritySchemes(AUTH_NAME, getSecurityScheme());
    }

    private SecurityScheme getSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .name(AUTH_NAME)
                .scheme("Bearer")
                .bearerFormat("JWT")
                .description("토큰을 입력해주세요.");
    }
}
