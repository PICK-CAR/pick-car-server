package dev.bang.pickcar.member.dto;

import dev.bang.pickcar.member.entity.Member;
import java.time.LocalDate;

public record MemberResponse(
        long memberId,
        String email,
        String name,
        String nickname,
        String phoneNumber,
        LocalDate birthDay
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getNickname(),
                member.getPhoneNumber(),
                member.getBirthDay()
        );
    }
}
