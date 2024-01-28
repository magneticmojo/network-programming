package keyhandler;
/**
 * Service to interact with the console for user input related to KeyStore operations.
 */
public interface ConsoleService {

    /**
     * Prompts the user to create a password for a new KeyStore. The user will be asked
     * to confirm the password.
     *
     * @return The password as a char array.
     */
    char[] createKeystorePassword();
    /**
     * Prompts the user to provide a password, typically used for accessing a KeyStore.
     *
     * @return The provided password as a char array.
     */
    char[] promptForPassword();
}
