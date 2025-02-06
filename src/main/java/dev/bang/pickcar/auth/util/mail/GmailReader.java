package dev.bang.pickcar.auth.util.mail;

import dev.bang.pickcar.global.config.properties.MailProperties;
import jakarta.annotation.PostConstruct;
import jakarta.mail.*;
import jakarta.mail.search.*;
import java.util.Comparator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
@EnableConfigurationProperties(MailProperties.class)
public class GmailReader implements MailReader {

    private static final String PROTOCOL = "imaps";
    private static final String FOLDER_INBOX = "INBOX";
    private static final String MIME_TYPE_MULTIPART = "multipart/*";
    private static final String MIME_TYPE_TEXT_PLAIN = "text/plain";
    private static final String MIME_TYPE_TEXT_HTML = "text/html";
    private static final int SEARCH_PERIOD_DAYS = 1;
    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");

    private final MailProperties mailProperties;
    private Session session;
    private Store store;

    @PostConstruct
    private void init() {
        JavaMailSenderImpl mailSender = createMailSender();
        session = mailSender.getSession();
        log.info("GmailReader 초기화 완료");
    }

    @Override
    public String findLatestMessageBySenderPhoneNumber(String phoneNumber) {
        try {
            Folder inbox = openInbox();
            Message latestMessage = findLatestMessageByPhoneNumber(inbox, phoneNumber);
            if (latestMessage == null) {
                return null;
            }
            return extractMessageContent(latestMessage);
        } catch (Exception e) {
            log.error("GmailReader 휴대폰 번호: {}로 이메일 조회 중 오류가 발생했습니다.", phoneNumber, e);
            throw new IllegalStateException("이메일 조회 중 오류가 발생했습니다.");
        } finally {
            closeConnection();
        }
    }

    private JavaMailSenderImpl createMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailProperties.imapServer());
        mailSender.setPort(mailProperties.imapPort());
        mailSender.setUsername(mailProperties.account());
        mailSender.setPassword(mailProperties.password());
        mailSender.setProtocol(PROTOCOL);
        addProps(mailSender);
        return mailSender;
    }

    private void addProps(JavaMailSenderImpl mailSender) {
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.store.protocol", PROTOCOL);
        props.put("mail.imaps.host", mailProperties.imapServer());
        props.put("mail.imaps.port", mailProperties.imapPort());
        props.put("mail.imaps.ssl.enable", "true");
        props.put("mail.imaps.starttls.enable", "true");
        props.put("mail.imaps.auth", "true");
    }

    private Folder openInbox() throws MessagingException {
        store = session.getStore(PROTOCOL);
        store.connect(mailProperties.imapServer(), mailProperties.account(), mailProperties.password());
        Folder inbox = store.getFolder(FOLDER_INBOX);
        inbox.open(Folder.READ_ONLY);
        log.info("GmailReader INBOX 폴더 열기 완료");
        return inbox;
    }

    /**
     * 휴대폰 번호로 발신된 메일 찾기, 가장 최근 메일을 반환
     */
    private Message findLatestMessageByPhoneNumber(Folder inbox, String phoneNumber) throws MessagingException {
        Message[] messages = searchMessages(inbox);
        if (messages.length == 0) {
            return null;
        }
        return findFirstMatchingMessage(messages, phoneNumber);
    }

    private Message[] searchMessages(Folder inbox) throws MessagingException {
        SearchTerm searchTerm = createDateSearchTerm();
        return inbox.search(searchTerm);
    }

    /**
     * 메일 검색 기간 설정
     */
    private SearchTerm createDateSearchTerm() {
        ZonedDateTime searchStartTime = ZonedDateTime.now(UTC_ZONE).minusDays(SEARCH_PERIOD_DAYS);
        Date startDate = Date.from(searchStartTime.toInstant());
        return new ReceivedDateTerm(ReceivedDateTerm.GT, startDate);
    }

    private Message findFirstMatchingMessage(Message[] messages, String phoneNumber) {
        return Arrays.stream(messages)
                .sorted(getMailDateComparator())
                .filter(message -> isPhoneNumberMatch(message, phoneNumber))
                .findFirst()
                .orElse(null);
    }

    private Comparator<Message> getMailDateComparator() {
        return (m1, m2) -> {
            try {
                return m2.getSentDate().compareTo(m1.getSentDate());
            } catch (MessagingException e) {
                log.warn("메일 날짜 비교 중 오류가 발생했습니다.", e);
                return 0;
            }
        };
    }

    private boolean isPhoneNumberMatch(Message message, String phoneNumber) {
        try {
            Address[] fromAddresses = message.getFrom();
            if (fromAddresses == null || fromAddresses.length == 0) {
                return false;
            }
            return fromAddresses[0].toString().startsWith(phoneNumber);
        } catch (MessagingException e) {
            log.warn("휴대폰 번호 발신자 확인 중 오류가 발생했습니다.", e);
            return false;
        }
    }

    private String extractMessageContent(Message message) throws Exception {
        if (message.isMimeType(MIME_TYPE_MULTIPART)) {
            return extractMultipartContent(message);
        }
        if (message.isMimeType(MIME_TYPE_TEXT_PLAIN) || message.isMimeType(MIME_TYPE_TEXT_HTML)) {
            return message.getContent().toString();
        }
        throw new IllegalArgumentException("지원하지 않는 이메일 형식입니다.");
    }

    private String extractMultipartContent(Message message) throws Exception {
        StringBuilder contentBuilder = new StringBuilder();
        Multipart multipart = (Multipart) message.getContent();

        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart part = multipart.getBodyPart(i);
            if (isTextContent(part)) {
                contentBuilder.append(part.getContent().toString());
            }
        }
        return contentBuilder.toString();
    }

    private boolean isTextContent(BodyPart part) throws MessagingException {
        return part.isMimeType(MIME_TYPE_TEXT_PLAIN) || part.isMimeType(MIME_TYPE_TEXT_HTML);
    }

    private void closeConnection() {
        try {
            if (store != null && store.isConnected()) {
                store.close();
            }
        } catch (MessagingException e) {
            log.error("GmailReader 연결을 닫는 중 오류가 발생했습니다.", e);
        }
    }
}
