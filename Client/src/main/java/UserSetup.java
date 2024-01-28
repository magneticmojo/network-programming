import java.io.IOException;
/**
 * Interface defining methods for user setup in the chat client.
 * The setup process includes setting the user alias, printing connection details, and displaying a welcome message.
 */
public interface UserSetup {

    /**
     * Prompts the user to set their chat alias.
     *
     * @throws IOException if an error occurs while reading the user input.
     */
    void setUserAlias() throws IOException;

    /**
     * Retrieves the user's set alias.
     *
     * @return The user's chat alias.
     */
    String getAlias();

    /**
     * Prints the connection details including IP and hostname.
     *
     * @param inetAddressHostAddress The IP address of the remote host.
     * @param inetAddressHostName The host name of the remote host.
     */
    void printConnectionDetails(String inetAddressHostAddress, String inetAddressHostName);

    /**
     * Prints a welcome message for the user.
     */
    void printWelcomeMsg();
}
