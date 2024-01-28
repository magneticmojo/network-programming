import jakarta.mail.*;
import jakarta.mail.internet.MimeMultipart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Handles the retrieval of emails based on a given configuration.
 * Connects to the email server, fetches emails and converts them to a list of {@link EmailMessage}.
 */
public class EmailFetcher {

    private static final String PORT = "993";
    private final Configuration config;

    /**
     * Constructs an EmailFetcher with the specified configuration.
     *
     * @param config the configuration to be used for email retrieval
     */
    public EmailFetcher(Configuration config) {
        this.config = config;
    }

    /**
     * Fetches emails based on the provided configuration.
     *
     * @return a list of retrieved email messages
     */
    public List<EmailMessage> fetchEmails() {
        List<EmailMessage> emailMessages = new ArrayList<>();
        try (Store store = connectToStore(); Folder inbox = openInbox(store)) {
            if (inbox != null) {
                Message[] messages = inbox.getMessages(1, Math.min(inbox.getMessageCount(), config.numEmails));
                for (Message message : messages) {
                    emailMessages.add(convertToEmailMessage(message));
                }
            }
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
        return emailMessages;
    }

    /**
     * Connects to the email server using the provided configuration.
     *
     * @return the connected email store
     * @throws MessagingException if a connection failure occurs
     */
    private Store connectToStore() throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.imap.host", config.emailServer);
        properties.put("mail.imap.port", PORT);
        properties.put("mail.imap.ssl.enable", "true");

        Session session = Session.getDefaultInstance(properties);
        Store store = session.getStore("imaps");
        store.connect(config.emailServer, config.email, config.password);
        return store;
    }

    /**
     * Opens the INBOX folder of the connected email store.
     *
     * @param store the connected email store
     * @return the opened INBOX folder or null if the INBOX doesn't exist
     * @throws MessagingException if an error occurs during opening
     */
    private Folder openInbox(Store store) throws MessagingException {
        Folder inbox = store.getFolder("INBOX");
        if (inbox.exists()) {
            inbox.open(Folder.READ_ONLY);
            return inbox;
        }
        return null;
    }

    /**
     * Converts a jakarta.mail.Message to an {@link EmailMessage}.
     *
     * @param message the jakarta.mail.Message to convert
     * @return the converted EmailMessage
     * @throws MessagingException if an error occurs during conversion
     * @throws IOException if an I/O error occurs
     */
    private EmailMessage convertToEmailMessage(Message message) throws MessagingException, IOException {
        String from = message.getFrom()[0].toString();
        String subject = message.getSubject();
        String content = getTextFromMessage(message);
        return new EmailMessage(from, subject, content);
    }

    /**
     * Retrieves the text content from a jakarta.mail.Message.
     *
     * @param message the jakarta.mail.Message to retrieve text from
     * @return the extracted text content
     * @throws MessagingException if an error occurs during extraction
     * @throws IOException if an I/O error occurs
     */
    private String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    /**
     * Retrieves the text content from a MimeMultipart.
     *
     * @param mimeMultipart the MimeMultipart to retrieve text from
     * @return the extracted text content
     * @throws MessagingException if an error occurs during extraction
     * @throws IOException if an I/O error occurs
     */
    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        StringBuilder result = new StringBuilder();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append("\n").append(bodyPart.getContent());
                break;  // without break same text appears twice in some cases
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result.append("\n").append(html);
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        return result.toString();
    }
}

