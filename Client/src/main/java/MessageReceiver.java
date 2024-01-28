import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles the task of receiving messages from the server.
 * Messages from the client themselves (matched by UUID) are not displayed.
 * This class is meant to be run in its own thread.
 */
public class MessageReceiver implements Runnable {

    private static final String UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
    private final Socket socket;
    private final UUID clientUUID;
    private final AtomicBoolean shouldExit;
    private final Pattern uuidPattern;

    /**
     * Constructs a new MessageReceiver instance.
     *
     * @param socket       the socket connected to the server
     * @param clientUUID   the unique identifier for the client
     * @param shouldExit   a flag indicating when to stop receiving messages
     */
    public MessageReceiver(Socket socket, UUID clientUUID, AtomicBoolean shouldExit) {
        this.socket = socket;
        this.clientUUID = clientUUID;
        this.shouldExit = shouldExit;
        this.uuidPattern = Pattern.compile(UUID_REGEX);
    }

    /**
     * Continuously reads incoming messages from the server and displays them.
     * The loop stops if an error occurs, if the connection fails, or if the `shouldExit` flag is set.
     */
    @Override
    public void run() {
        try (BufferedReader in = setupBufferedReader()) {
            while (!shouldExit.get()) {
                String line = in.readLine();
                if (line == null) {
                    System.out.println("Connection failure, press any key to shut down");
                    shouldExit.set(true);
                    break;
                }

                if (containsUUID(line)) {
                    if (startsWithDifferentUUID(line)) {
                        System.out.println(extractMessage(line));
                    }
                } else {
                    System.out.println(line);
                }
            }
        } catch (SocketTimeoutException ste) {
            System.out.println("Socket timeout expired. Terminating connection. Press any key to shut down.");
            shouldExit.set(true);
        } catch (IOException e) {
            System.out.println("IOException thrown. Terminating connection.");
            shouldExit.set(true);
        }
    }

    /**
     * Determines if the given line contains a UUID.
     *
     * @param line the input string
     * @return true if the line contains a UUID, false otherwise
     */
    private boolean containsUUID(String line) {
        Matcher matcher = uuidPattern.matcher(line);
        return matcher.find();
    }

    /**
     * Checks if the line starts with a UUID that is different from the client's UUID.
     *
     * @param line the input string
     * @return true if the line starts with a different UUID, false otherwise
     */
    private boolean startsWithDifferentUUID(String line) {
        return !line.split("\\|", 2)[0].equals(clientUUID.toString());
    }

    /**
     * Extracts the message from a line that contains a UUID.
     *
     * @param line the input string with UUID|message format
     * @return the message without the UUID
     */
    private String extractMessage(String line) {
        return line.split("\\|", 2)[1];
    }

    /**
     * Initializes and returns a BufferedReader to read messages from the server.
     *
     * @return BufferedReader for reading messages
     * @throws IOException if an error occurs during BufferedReader setup
     */
    private BufferedReader setupBufferedReader() throws IOException {
        InputStream inputStream = socket.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.ISO_8859_1);
        return new BufferedReader(inputStreamReader);
    }
}