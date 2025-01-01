package dev.bang.pickcar.member.controller;

import dev.bang.pickcar.auth.util.LoginMemberId;
import dev.bang.pickcar.member.controller.docs.MemberApiDocs;
import dev.bang.pickcar.member.dto.MemberResponse;
import dev.bang.pickcar.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("members")
public class MemberController implements MemberApiDocs {

    private final MemberService memberService;

    @GetMapping("me")
    @PreAuthorize("hasRole('MEMBER')")
    @Override
    public ResponseEntity<MemberResponse> myInfo(@LoginMemberId Long id) {
        return ResponseEntity.ok(memberService.getMemberInfo(id));
    }
}
