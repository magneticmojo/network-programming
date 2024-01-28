import java.util.List;
import java.util.concurrent.Callable;

/**
 * A callable task responsible for retrieving email messages using an {@link EmailFetcher}.
 * This task can be used in conjunction with thread executors.
 */
class EmailReceiverTask implements Callable<List<EmailMessage>> {

    private final EmailFetcher fetcher;

    /**
     * Constructs an EmailReceiverTask with the specified parameters for email retrieval.
     *
     * @param emailServer the address of the email server
     * @param username the user's email address
     * @param password the password for the email account
     * @param numEmails the number of emails to retrieve
     */
    public EmailReceiverTask(String emailServer, String username, String password, int numEmails) {
        Configuration config = new Configuration(emailServer, username, password, numEmails);
        this.fetcher = new EmailFetcher(config);
    }

    /**
     * Retrieves the email messages using the configured {@link EmailFetcher}.
     *
     * @return a list of retrieved email messages
     */
    @Override
    public List<EmailMessage> call() {
        return fetcher.fetchEmails();
    }
}
