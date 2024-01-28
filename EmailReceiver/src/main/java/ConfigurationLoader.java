import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * A utility class for loading email configurations.
 * The configuration can be loaded from command line arguments or a properties file.
 */
public class ConfigurationLoader {

    private static final int DEFAULT_NUM_EMAILS = 2;

    /**
     * Loads the email configuration based on the given command line arguments.
     *
     * @param args the command line arguments
     * @return a Configuration object, or null if the arguments are invalid or missing
     */
    public Configuration loadConfiguration(String[] args) {
        if (args.length == 0) {
            return loadFromProperties();
        } else if (args.length >= 3) {
            String emailServer = args[0];
            String email = args[1];
            String password = args[2];
            int numEmails = args.length == 4 ? parseInt(args[3], DEFAULT_NUM_EMAILS) : DEFAULT_NUM_EMAILS;
            return new Configuration(emailServer, email, password, numEmails);
        }
        return null;
    }

    /**
     * Parses the given string to an integer. Returns the default value if parsing fails.
     *
     * @param s the string to be parsed
     * @param defaultValue the default integer value to be returned in case of a parsing failure
     * @return the parsed integer or the default value
     */
    private int parseInt(String s, int defaultValue) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.out.println("Invalid format. Using default value.");
            return defaultValue;
        }
    }

    /**
     * Loads the email configuration from a properties file named "config.properties".
     *
     * @return the loaded configuration, or null if loading failed
     */
    private Configuration loadFromProperties() {
        Properties properties = new Properties();
        try (InputStream in = EmailReceiverApp.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(in);
            return new Configuration(
                    properties.getProperty("email.server", "imap.gmail.com"),
                    properties.getProperty("email.address"),
                    properties.getProperty("email.password"),
                    Integer.parseInt(properties.getProperty("num.emails", String.valueOf(DEFAULT_NUM_EMAILS)))
            );
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
