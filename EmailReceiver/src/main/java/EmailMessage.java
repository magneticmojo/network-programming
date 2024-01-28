/**
 * Represents an email message with sender, subject, and content fields.
 */
public class EmailMessage {
    private final String from;
    private final String subject;
    private final String content;

    /**
     * Constructs an EmailMessage with the specified sender, subject, and content.
     *
     * @param from the sender of the email
     * @param subject the subject of the email
     * @param content the content of the email
     */
    public EmailMessage(String from, String subject, String content) {  // Updated constructor
        this.from = from;
        this.subject = subject;
        this.content = content;
    }

    @Override
    public String toString() {
        return "From: " + from + "\nSubject: " + subject + "\nContent: " + content;
    }
}
