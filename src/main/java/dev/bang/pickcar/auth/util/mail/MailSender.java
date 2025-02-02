package dev.bang.pickcar.auth.util.mail;

public interface MailSender {

    void send(String to, String subject, String text);
}
