package dev.bang.pickcar.auth.controller.docs;

import dev.bang.pickcar.auth.dto.EmailVerifyRequest;
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

    @Operation(summary = "이메일 중복 확인", description = "이메일 중복을 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 중복 확인 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<Boolean> checkEmailDuplication(String email);

    @Operation(summary = "(이메일) 인증번호 전송", description = "이메일 인증을 위한 인증번호를 전송합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증번호 전송 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<Void> sendVerificationCodeToEmail(String email);

    @Operation(summary = "(이메일) 인증번호 확인", description = "이메일 인증을 위한 인증번호를 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증번호 확인 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<Void> verifyEmail(EmailVerifyRequest emailVerifyRequest);

    @Operation(summary = "인증번호 발급", description = "휴대폰 번호 인증을 위한 인증번호를 발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증번호 발급 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<String> issueVerificationCode(String phoneNumber);

    @Operation(summary = "인증번호 확인", description = "휴대폰 번호 인증을 위한 인증번호를 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증번호 확인 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<Void> verifyVerificationNumber(String phoneNumber);
}
