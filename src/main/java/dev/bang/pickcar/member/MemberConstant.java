package dev.bang.pickcar.member;

public class MemberConstant {

    public static final int MIN_NICKNAME_LENGTH = 2;
    public static final int MAX_NICKNAME_LENGTH = 10;

    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    public static final String BIRTHDAY_REGEX = "\\d{4}-\\d{2}-\\d{2}";
    public static final String BIRTHDAY_FORMAT = "yyyy-MM-dd";

    public static final String PHONE_NUMBER_REGEX = "^010\\d{8}$";
    public static final String PHONE_NUMBER_FORMAT = "01000000000";

    private MemberConstant() {
    }
}
