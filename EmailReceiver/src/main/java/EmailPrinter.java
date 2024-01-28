import java.util.List;

/**
 * Utility class responsible for printing a list of {@link EmailMessage} to the standard output.
 */
public class EmailPrinter {

    /**
     * Prints the list of email messages to the standard output.
     *
     * @param emailMessages the list of email messages to print
     */
    public void print(List<EmailMessage> emailMessages) {
        emailMessages.forEach(System.out::println);
    }
}
