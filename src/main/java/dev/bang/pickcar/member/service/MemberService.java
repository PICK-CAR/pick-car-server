package dev.bang.pickcar.member.service;

import dev.bang.pickcar.member.dto.MemberRequest;
import dev.bang.pickcar.member.dto.MemberResponse;
import dev.bang.pickcar.member.entity.Member;
import dev.bang.pickcar.member.repository.MemberQueryRepository;
import dev.bang.pickcar.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberQueryRepository memberQueryRepository;

    @Transactional
    public Long create(MemberRequest memberRequest, String encryptedPassword) {
        if (memberQueryRepository.existsByEmail(memberRequest.email())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        Member member = memberRequest.toMember(encryptedPassword);
        return memberRepository.save(member)
                .getId();
    }

    @Transactional(readOnly = true)
    public MemberResponse getMemberInfo(Long id) {
        return memberQueryRepository.findMemberResponseById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
    }

    public boolean existsByEmail(String email) {
        return memberQueryRepository.existsByEmail(email);
    }
}
