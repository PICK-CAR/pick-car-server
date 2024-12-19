package dev.bang.pickcar.member;

public class MemberConstant {

    public static final int MIN_NICKNAME_LENGTH = 2;
    public static final int MAX_NICKNAME_LENGTH = 10;

    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    public static final String BIRTHDAY_REGEX = "\\d{4}-\\d{2}-\\d{2}";
    public static final String BIRTHDAY_FORMAT = "yyyy-MM-dd";

    public static final String PHONE_NUMBER_REGEX = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}";
    public static final String PHONE_NUMBER_FORMAT = "010-0000-0000";

    private MemberConstant() {
    }
}
