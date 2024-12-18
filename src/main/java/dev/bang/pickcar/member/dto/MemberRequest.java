package dev.bang.pickcar.member.dto;

import dev.bang.pickcar.member.entity.Member;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record MemberRequest(
        String name,
        String nickname,
        String email,
        String password,
        String birthDay,
        String phoneNumber
) {
    public Member toMember(String encryptedPassword) {
        return new Member(name, nickname, email, encryptedPassword, parseBirthDay(), phoneNumber);
    }

    private LocalDate parseBirthDay() {
        return LocalDate.parse(birthDay, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
