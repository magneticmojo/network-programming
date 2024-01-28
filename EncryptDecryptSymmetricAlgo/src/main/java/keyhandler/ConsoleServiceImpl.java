package keyhandler;

import java.io.Console;
import java.util.Arrays;

/**
 * Provides a concrete implementation of the {@link ConsoleService} interface.
 * This implementation communicates directly with the system console to collect user input related
 * to keystore passwords.
 */
public class ConsoleServiceImpl implements ConsoleService {

    /**
     * Prompts the user to create a password for the keystore through the system console.
     * The user is required to enter the password twice to confirm its correctness.
     * The process is repeated until the two entries match.
     *
     * @return A character array containing the created password.
     */
    @Override
    public char[] createKeystorePassword() {
        Console console = System.console();
        if (console == null) {
            System.out.println("Couldn't get Console instance");
            System.exit(0);
        }

        while (true) {
            char[] password = console.readPassword("Enter password: ");
            char[] confirmPassword = console.readPassword("Confirm password: ");
            if (Arrays.equals(password, confirmPassword)) {
                Arrays.fill(confirmPassword, '0');
                return password;
            }
            System.out.println("Passwords don't match! Try again.");
        }
    }

    /**
     * Prompts the user to provide the keystore password through the system console.
     *
     * @return A character array containing the provided password.
     */
    @Override
    public char[] promptForPassword() {
        Console console = System.console();
        if (console == null) {
            System.out.println("Couldn't get Console instance");
            System.exit(0);
        }
        return console.readPassword("Enter keystore password: ");
    }
}

