package dev.bang.pickcar.member.controller.docs;

import dev.bang.pickcar.member.dto.MemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Member", description = "회원 정보 API")
public interface MemberApiDocs {

    @Operation(summary = "내 정보 조회", description = "내 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내 정보 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<MemberResponse> myInfo(@Parameter(hidden = true) Long id);
}
