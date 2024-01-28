import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * Represents a chat client responsible for connecting to a chat server.
 * The client can send and receive text messages through a stream socket connection.
 * Upon successful connection, any message sent by the client is broadcasted
 * by the server to all connected clients.
 *
 * <p>Usage:
 * <pre>
 *     java Client (DEFAULT_HOST=127.0.0.1, DEFAULT_PORT=2000)
 *     java Client &lt;host&gt; (DEFAULT_PORT=2000)
 *     java Client &lt;host&gt; &lt;port&gt;
 * </pre>
 * </p>
 */
public class Client {

    private static final String DEFAULT_SERVER_HOST = "127.0.0.1";
    private static final int DEFAULT_SERVER_PORT = 2000;
    private static final int MIN_USER_PORT = 1024;
    private static final int MAX_USER_PORT = 65535;
    private final String serverHost;
    private final int serverPort;

    /**
     * Creates an instance of the client and initiates the connection process.
     *
     * @param serverHost the server host address
     * @param serverPort the port number on the server
     */
    public Client(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    /**
     * Attempts to establish a connection with the server.
     * Handles various exceptions that may arise during the connection process.
     */
    private void connect() {
        try (ClientConnection connection = new ClientConnection(serverHost, serverPort)) { // Closeable calls close()
            connection.initialize();
            connection.waitForWorkerCompletion();
        } catch (SocketTimeoutException e){
            System.out.println("Socket timeout expired. Terminating connection.");
        } catch (InterruptedException e) {
            System.out.println("Interruption exception. Shutting down.");
        } catch (IOException e) {
            System.out.println("No server connection. Shutting down.");
        } catch (RuntimeException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    /**
     * Entry point for the client application.
     * Initializes the client based on command-line arguments and establishes a connection.
     *
     * @param args command-line arguments provided on startup
     */
    public static void main(String[] args) {
        Client client = initClientFromArgs(args);
        client.connect();
    }

    /**
     * Initializes the client based on provided command-line arguments.
     *
     * @param args command-line arguments provided on startup.
     * @return an instance of the Client initialized with the appropriate host and port.
     */
    private static Client initClientFromArgs(String[] args) {
        return switch (args.length) {
            case 1 -> createClientWithDefaultServerPort(args[0]);
            case 2 -> createClientWithSpecifiedPort(args[0], args[1]);
            default -> createDefaultClient();
        };
    }

    /**
     * Creates a client that connects to the provided server host and uses the default server port.
     *
     * @param serverHost the server host to connect to.
     * @return a new Client instance initialized with the provided host and default port.
     */
    private static Client createClientWithDefaultServerPort(String serverHost) {
        return new Client(serverHost, DEFAULT_SERVER_PORT);
    }

    /**
     * Creates a client based on the provided host and port arguments.
     *
     * @param host the server host to connect to.
     * @param portArg the server port to connect to.
     * @return a new Client instance initialized with the provided host and port.
     */
    private static Client createClientWithSpecifiedPort(String host, String portArg) {
        int port = parsePortOrDefault(portArg);
        return new Client(host, port);
    }

    private static int parsePortOrDefault(String portArg) {
        try {
            int port = Integer.parseInt(portArg);
            if (isValidPortRange(port)) {
                return port;
            }
            System.out.println("Well-known ports not accepted. Using default");
        } catch (NumberFormatException e) {
            System.out.println("Invalid port number. Using default");
        }
        return DEFAULT_SERVER_PORT;
    }

    /**
     * Checks if the given port number is within the valid range of user ports.
     * Accepts registered ports (1024 - 49151) and dynamic ports (49152 - 65535),
     * but excludes well-known ports (0 - 1023).
     *
     * @param port the port number to be checked.
     * @return true if the port is within the valid user port range, false otherwise.
     */
    private static boolean isValidPortRange(int port) {
        return port >= MIN_USER_PORT && port <= MAX_USER_PORT;
    }

    /**
     * Creates a client with default host and port values.
     *
     * @return a new Client instance initialized with default values for host and port.
     */
    private static Client createDefaultClient() {
        return new Client(DEFAULT_SERVER_HOST, DEFAULT_SERVER_PORT);
    }
}
