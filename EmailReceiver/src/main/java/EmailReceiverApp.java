import java.util.List;
import java.util.concurrent.*;

/**
 * The EmailReceiverApp is an application designed to fetch emails based on a given configuration.
 * It uses the {@link EmailReceiverTask} to fetch emails in a separate thread and then prints the
 * fetched emails.
 * The application starts by loading the configuration, either from the command line arguments
 * or from the properties file. If the configuration is valid, it submits the {@link EmailReceiverTask}
 * to fetch the emails. Once the emails are fetched, they are printed using the {@link EmailPrinter}.
 */
public class EmailReceiverApp {

    /**
     * Fetches emails using the EmailReceiverTask.
     *
     * @param config the configuration containing email settings
     * @return a list of email messages
     */
    private static List<EmailMessage> fetchEmailsUsingTask(Configuration config) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<List<EmailMessage>> futureMessages = executor.submit(new EmailReceiverTask(
                config.emailServer, config.email, config.password, config.numEmails));

        List<EmailMessage> emailMessages = null;
        try {
            emailMessages = futureMessages.get();  // This will block until the task is finished.
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Failed to fetch emails.");
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        return emailMessages;
    }

    /**
     * Prints a list of email messages.
     *
     * @param emailMessages the list of email messages to print
     */
    private static void printEmailMessages(List<EmailMessage> emailMessages) {
        EmailPrinter printer = new EmailPrinter();
        printer.print(emailMessages);
    }

    /**
     * The main entry point for the EmailReceiverApp.
     * Initializes the configuration, fetches the emails based on that configuration and then prints them.
     *
     * @param args the command line arguments. Can include the email server, email address, password, and optionally, the number of emails to fetch.
     */
    public static void main(String[] args) {
        ConfigurationLoader configLoader = new ConfigurationLoader();
        Configuration config = configLoader.loadConfiguration(args);

        if (config == null) {
            System.out.println("Invalid configuration.");
            return;
        }

        List<EmailMessage> emailMessages = fetchEmailsUsingTask(config);

        if (emailMessages != null) {
            printEmailMessages(emailMessages);
        }
    }
}
