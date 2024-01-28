/**
 * Represents the email configuration used by the application.
 */
public class Configuration {
    String emailServer;
    String email;
    String password;
    int numEmails;

    /**
     * Constructs a new Configuration object with the given parameters.
     *
     * @param emailServer the address of the email server
     * @param email the user's email address
     * @param password the password for the email account
     * @param numEmails the number of emails to retrieve
     */
    Configuration(String emailServer, String email, String password, int numEmails) {
        this.emailServer = emailServer;
        this.email = email;
        this.password = password;
        this.numEmails = numEmails;
    }
}
