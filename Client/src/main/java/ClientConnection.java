import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents a connection for the client to communicate with the chat server.
 * This class handles the initialization, setting up of user details, and the initiation
 * of sender and receiver threads for the client's chat operations.
 */
public class ClientConnection implements Closeable {

    private static final int SOCKET_TIME_OUT = 60000;
    private final Socket socket;
    private final UUID clientUUID;
    private final AtomicBoolean shouldExit;
    private final InputStreamReader in;
    private final BufferedReader userInput;
    private final UserSetup userSetup;
    private Thread senderThread;
    private Thread receiverThread;

    /**
     * Constructs a new client connection.
     *
     * @param host the server host to connect to
     * @param port the server port to connect to
     * @throws IOException if an error occurs during the connection process
     */
    public ClientConnection(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.clientUUID = UUID.randomUUID();
        this.shouldExit = new AtomicBoolean();
        this.in = new InputStreamReader(System.in);
        this.userInput = new BufferedReader(in);
        this.userSetup = new UserSetupCli(userInput);
    }

    /**
     * Initializes the client connection by setting the user's alias,
     * configuring the socket timeout, and starting worker threads for
     * message sending and receiving.
     *
     * @throws IOException if an error occurs during the initialization process
     */
    public void initialize() throws IOException {
        userSetup.setUserAlias();
        socket.setSoTimeout(SOCKET_TIME_OUT);
        if (isConnected()) {
            userSetup.printConnectionDetails(getHostAddress(socket), getHostName(socket));
            userSetup.printWelcomeMsg();
            startWorkerThreads();
        }
    }

    /**
     * Checks if the socket is connected and not closed.
     *
     * @return true if the socket is connected and not closed, false otherwise.
     */
    private boolean isConnected() {
        return socket.isConnected() && !socket.isClosed();
    }

    /**
     * Starts separate threads to handle incoming messages (receiver) and outgoing messages (sender).
     */
    private void startWorkerThreads() {
            receiverThread = new Thread(new MessageReceiver(socket, clientUUID, shouldExit));
            receiverThread.start();

            senderThread = new Thread(new MessageSender(socket, clientUUID, shouldExit, userInput, userSetup.getAlias()));
            senderThread.start();
    }

    /**
     * Waits for the completion of sender and receiver threads.
     *
     * @throws InterruptedException if the waiting process is interrupted
     */
    public void waitForWorkerCompletion() throws InterruptedException {
        senderThread.join();
        receiverThread.join();
    }

    /**
     * Closes the connection and interrupts the worker threads.
     *
     * @throws IOException if an error occurs while closing the connection
     */
    @Override
    public void close() throws IOException {
        socket.close();
        shouldExit.set(true);
        senderThread.interrupt();
        receiverThread.interrupt();
    }

    /**
     * Retrieves the IP address of the provided socket's remote endpoint.
     *
     * @param socket the socket whose remote IP address is to be returned.
     * @return a string representing the IP address of the remote endpoint.
     */
    private String getHostAddress(Socket socket) {
        InetAddress inetAddress = socket.getInetAddress();
        return inetAddress.getHostAddress();
    }

    /**
     * Retrieves the hostname of the provided socket's remote endpoint.
     *
     * @param socket the socket whose remote hostname is to be returned.
     * @return a string representing the hostname of the remote endpoint.
     */
    private String getHostName(Socket socket) {
        InetAddress inetAddress = socket.getInetAddress();
        return inetAddress.getHostName();
    }
}

