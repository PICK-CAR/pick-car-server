package dev.bang.pickcar.auth.controller.docs;

import dev.bang.pickcar.auth.dto.LoginRequest;
import dev.bang.pickcar.auth.dto.TokenResponse;
import dev.bang.pickcar.member.dto.MemberRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth", description = "회원가입 및 로그인 API")
public interface AuthApiDocs {

    @Operation(summary = "회원가입", description = "회원가입을 수행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<Void> signup(MemberRequest memberRequest);

    @Operation(summary = "로그인", description = "로그인을 수행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<TokenResponse> login(LoginRequest loginRequest);
}
