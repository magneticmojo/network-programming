import java.io.InputStream;
import java.util.Properties;

/**
 * Represents the configuration for sending emails. This includes
 * server details, port, username, and password. If not provided,
 * certain default values or values from properties files are used.
 */
public class Configuration {
    private Properties properties;
    private String emailServer;
    private int port;
    private String username;
    private String password;

    /**
     * Constructs a Configuration instance and loads default properties from the "config.properties" file.
     */
    public Configuration() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the email server to use.
     *
     * @return The specified email server if set, or the default "smtp.gmail.com" if not.
     */
    public String getEmailServer() {
        return (emailServer != null && !emailServer.isEmpty()) ? emailServer : "smtp.gmail.com";
    }

    public void setEmailServer(String emailServer) {
        this.emailServer = emailServer;
    }

    /**
     * Retrieves the port to use for the email server.
     *
     * @return The specified port if set, or the default port 587 if not.
     */
    public int getPort() {
        return (port != 0) ? port : 587;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Retrieves the username for email authentication.
     *
     * @return The specified username if set, or the one from properties if not.
     */
    public String getUsername() {
        return (username != null && !username.isEmpty()) ? username : properties.getProperty("email.address");
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retrieves the password for email authentication.
     *
     * @return The specified password if set, or the one from properties if not.
     */
    public String getPassword() {
        return (password != null && !password.isEmpty()) ? password : properties.getProperty("email.password");
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
