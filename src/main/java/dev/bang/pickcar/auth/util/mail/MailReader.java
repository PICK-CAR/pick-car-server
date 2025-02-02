package dev.bang.pickcar.auth.util.mail;

public interface MailReader {

    String findLatestMessageBySenderPhoneNumber(String phoneNumber);
}
