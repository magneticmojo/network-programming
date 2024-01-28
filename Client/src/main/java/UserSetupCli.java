import java.io.BufferedReader;
import java.io.IOException;
import java.util.Random;

/**
 * Command line-based user setup utility for the chat client.
 * This class handles user alias configuration, as well as printing connection details and a welcome message.
 */
public class UserSetupCli implements UserSetup{

    private static final String DEFAULT_ALIAS = "No_Name";
    private final BufferedReader userInput;
    private String alias;

    /**
     * Constructs a new UserSetupCli using the provided BufferedReader.
     *
     * @param userInput a BufferedReader to read user input.
     */
    public UserSetupCli(BufferedReader userInput) {
        this.userInput = userInput;
    }

    /**
     * Reads a single line of user input.
     *
     * @return The line of text entered by the user.
     * @throws IOException if an error occurs while reading the input.
     */
    private String readLineFromUser() throws IOException {
        return userInput.readLine();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAlias() {
        if (alias == null) {
            throw new IllegalStateException("Alias has not been set yet.");
        }
        return alias;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUserAlias() throws IOException {
        System.out.println("Enter a user alias:");
        String alias = readLineFromUser();

        if (alias == null || alias.isEmpty() || alias.isBlank()) {
            alias = DEFAULT_ALIAS + new Random().nextInt(10000);
        }
        this.alias = alias;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printConnectionDetails(String inetAddressHostAddress, String inetAddressHostName) {
        String separatorLine = String.format("%1$-60s", "").replace(' ', '*');
        String ipLine = String.format("IP: %s", inetAddressHostAddress);
        String hostNameLine = String.format("Host name: %s", inetAddressHostName);

        System.out.println("***********************   SUCCESS   ************************");
        System.out.println("Socket connection established between local and remote host");
        System.out.println(separatorLine);
        System.out.println(ipLine);
        System.out.println(hostNameLine);
        System.out.println(separatorLine);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printWelcomeMsg() {
        System.out.println(">>> WELCOME TO THE CHAT " + alias + " <<<");
        System.out.println("Enter 'quit' to exit");
        System.out.println("************************************************************");
    }
}
