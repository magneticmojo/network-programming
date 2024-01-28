import javax.swing.*;

/**
 * SwingWorker implementation to send emails in the background, ensuring
 * the GUI remains responsive. On completion, notifies the user of the result.
 */
public class EmailWorker extends SwingWorker<Void, Void> {

    private EmailService emailService;
    private Email email;
    private Configuration config;

    /**
     * Constructor for the EmailWorker class.
     *
     * @param emailService The service to send emails.
     * @param email The email object with details to send.
     * @param config The configuration for the email service.
     */
    public EmailWorker(EmailService emailService, Email email, Configuration config) {
        this.emailService = emailService;
        this.email = email;
        this.config = config;
    }

    /**
     * Sends the email in a background thread.
     *
     * @return null as it's a Void SwingWorker.
     * @throws Exception If there's an error while sending the email.
     */
    @Override
    protected Void doInBackground() throws Exception {
        emailService.sendEmail(email, config);
        return null;
    }

    /**
     * Executed after the email is sent or fails. Notifies the user of the result.
     */
    @Override
    protected void done() {
        try {
            get();
            JOptionPane.showMessageDialog(null, "Email sent successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error sending email: " + e.getMessage());
        }
    }
}
