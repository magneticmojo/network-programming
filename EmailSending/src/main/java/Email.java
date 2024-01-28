/**
 * Represents an email with a recipient (to), subject, and message.
 * Ensures the email address format is valid upon creation.
 */
public class Email {

    private String to;
    private String subject;
    private String message;

    /**
     * Constructs an Email instance.
     *
     * @param to      The recipient's email address.
     * @param subject The subject of the email.
     * @param message The body/message of the email.
     * @throws InvalidEmailException If the provided 'to' email address is not in a valid format.
     */
    public Email(String to, String subject, String message) throws InvalidEmailException {
        if (isInValidEmail(to)) {
            throw new InvalidEmailException("Email format incorrect");
        }
        this.to = to;
        this.subject = subject;
        this.message = message;
    }

    /**
     * Checks if the provided email address matches a valid format.
     *
     * @param to The email address to check.
     * @return true if the email is invalid, false otherwise.
     */
    private boolean isInValidEmail(String to) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return !to.matches(regex);
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }
}
