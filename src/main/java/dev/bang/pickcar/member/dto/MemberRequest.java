package dev.bang.pickcar.member.dto;

import static dev.bang.pickcar.member.MemberConstant.BIRTHDAY_FORMAT;
import static dev.bang.pickcar.member.MemberConstant.BIRTHDAY_REGEX;
import static dev.bang.pickcar.member.MemberConstant.EMAIL_REGEX;
import static dev.bang.pickcar.member.MemberConstant.PHONE_NUMBER_FORMAT;
import static dev.bang.pickcar.member.MemberConstant.PHONE_NUMBER_REGEX;

import dev.bang.pickcar.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record MemberRequest(
        @NotBlank(message = "이름을 입력해주세요.")
        String name,
        @NotBlank(message = "닉네임을 입력해주세요.")
        String nickname,
        @Pattern(regexp = EMAIL_REGEX, message = "이메일 형식에 맞게 입력해주세요.")
        String email,
        @NotBlank(message = "비밀번호를 입력해주세요.")
        String password,
        @Pattern(regexp = BIRTHDAY_REGEX, message = "생일은 " + BIRTHDAY_FORMAT + " 형식으로 입력해주세요.")
        String birthDay,
        @Pattern(regexp = PHONE_NUMBER_REGEX, message = "휴대폰 번호는 " + PHONE_NUMBER_FORMAT + " 형식으로 입력해주세요.")
        String phoneNumber
) {
    public Member toMember(String encryptedPassword) {
        return new Member(name, nickname, email, encryptedPassword, parseBirthDay(), phoneNumber);
    }

    private LocalDate parseBirthDay() {
        return LocalDate.parse(birthDay, DateTimeFormatter.ofPattern(BIRTHDAY_FORMAT));
    }
}
