import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

/**
 * Service for sending emails. Utilizes the JavaMail API to send emails
 * based on given Email and Configuration objects.
 */
public class EmailService {

    /**
     * Sends an email using the provided Email and Configuration objects.
     *
     * @param email The email details to send.
     * @param config The configuration for the email service.
     * @throws MessagingException If there's an error during email sending.
     */
    public void sendEmail(Email email, Configuration config) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", config.getEmailServer());
        props.put("mail.smtp.port", config.getPort());

        Session session = Session.getInstance(props,
                new jakarta.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(config.getUsername(), config.getPassword());
                    }
                });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(config.getUsername()));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getTo()));
        message.setSubject(email.getSubject());
        message.setText(email.getMessage());

        Transport.send(message);
    }
}
