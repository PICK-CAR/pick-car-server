package dev.bang.pickcar.member.entity;

import static dev.bang.pickcar.member.MemberConstant.EMAIL_REGEX;
import static dev.bang.pickcar.member.MemberConstant.MAX_NICKNAME_LENGTH;
import static dev.bang.pickcar.member.MemberConstant.MIN_NICKNAME_LENGTH;
import static dev.bang.pickcar.member.MemberConstant.PHONE_NUMBER_REGEX;

import dev.bang.pickcar.global.entitiy.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Table(name = "members")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;

    private String name;

    private String nickname;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private LocalDate birthDay;

    private String phoneNumber;

    @Enumerated(value = EnumType.STRING)
    private MemberRole role;

    public Member(String name, String nickname, String email, String password, LocalDate birthDay, String phoneNumber) {
        validate(name, nickname, email, password, phoneNumber);
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.birthDay = birthDay;
        this.phoneNumber = phoneNumber;
        this.role = MemberRole.MEMBER;
    }

    public Member(String name, String nickname, String email, String password, String phoneNumber, MemberRole role) {
        validate(name, nickname, email, password, phoneNumber);
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    private void validate(String name, String nickname, String email, String password, String phoneNumber) {
        Assert.hasText(name, "이름은 필수로 입력해야 합니다.");
        validateNickname(nickname);
        validateEmail(email);
        Assert.hasText(password, "비밀번호는 필수로 입력해야 합니다.");
        validatePhoneNumber(phoneNumber);
    }

    private void validateNickname(String nickname) {
        Assert.hasText(nickname, "닉네임은 필수로 입력해야 합니다.");
        if (nickname.length() < MIN_NICKNAME_LENGTH || nickname.length() > MAX_NICKNAME_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("닉네임은 %d자 이상 %d자 이하로 입력해야 합니다.", MIN_NICKNAME_LENGTH, MAX_NICKNAME_LENGTH));
        }
    }

    private void validateEmail(String email) {
        Assert.hasText(email, "이메일은 필수로 입력해야 합니다.");
        if (!email.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        Assert.hasText(phoneNumber, "휴대폰 번호는 필수로 입력해야 합니다.");
        if (!phoneNumber.matches(PHONE_NUMBER_REGEX)) {
            throw new IllegalArgumentException("휴대폰 번호 형식이 올바르지 않습니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(name, member.name)
                && Objects.equals(email, member.email)
                && Objects.equals(password, member.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, password);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", birthDay=" + birthDay +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role=" + role +
                '}';
    }
}
