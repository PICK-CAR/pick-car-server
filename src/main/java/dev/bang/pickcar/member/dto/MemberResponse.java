package dev.bang.pickcar.member.dto;

import java.time.LocalDate;

public record MemberResponse(
        long memberId,
        String email,
        String name,
        String nickname,
        String phoneNumber,
        LocalDate birthDay
) {
}
