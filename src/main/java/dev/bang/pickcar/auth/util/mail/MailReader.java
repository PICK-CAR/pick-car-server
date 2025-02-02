package dev.bang.pickcar.auth.util;

public interface MailReader {

    String findLatestMessageBySenderPhoneNumber(String phoneNumber);
}
