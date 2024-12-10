package ru.rmntim.web.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Stateless
@Slf4j
public class EmailService {
    private Session emailSession;

    @Resource(lookup = "java:global/mail/host")
    private String SMTP_HOST;

    @Resource(lookup = "java:global/mail/port")
    private String SMTP_PORT;

    @Resource(lookup = "java:global/mail/password")
    private String SMTP_PASSWORD;

    @PostConstruct
    public void init() {
        var props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.ssl.trust", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        emailSession = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("API_KEY", SMTP_PASSWORD);
            }
        });
    }

    public void sendEmail(String email, String subject, String text) {
        try {
            var fromAddress = "noreply@mail.rmntim.ru";

            var message = new MimeMessage(emailSession);
            message.setFrom(new InternetAddress(fromAddress));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);
        } catch (MessagingException e) {
            log.error("Error sending email: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void sendSignUpEmail(String email, String username) {
        var subject = "Welcome to Our Service!";
        var text = "Hello " + username + ",\n\n" +
                "You have successfully signed up with the following credentials:\n" +
                "Username: " + username + "\n" +
                "Email: " + email + "\n" +
                "Do not forget your password and keep it safe.";

        sendEmail(email, subject, text);
    }

    public void sendPasswordChangeEmail(String email) {
        var subject = "Your Password Has Been Changed";
        var text = """
                Hello,
                
                The password for your account has successfully been changed.
                If you did not initiate this change, please contact our support team immediately.""";

        sendEmail(email, subject, text);
    }
}
