package dev.bang.pickcar.member.service;

import dev.bang.pickcar.member.dto.MemberRequest;
import dev.bang.pickcar.member.dto.MemberResponse;
import dev.bang.pickcar.member.entity.Member;
import dev.bang.pickcar.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long create(MemberRequest memberRequest, String encryptedPassword) {
        Member member = memberRequest.toMember(encryptedPassword);
        return memberRepository.save(member)
                .getId();
    }

    @Transactional(readOnly = true)
    public MemberResponse getMemberInfo(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
        return MemberResponse.from(member);
    }
}
