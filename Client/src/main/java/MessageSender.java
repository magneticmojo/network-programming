import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Handles the task of sending messages from the client to the server.
 * Prepends each message with the client's UUID and alias for message sender identification.
 * This class is meant to be run in its own thread.
 */
public class MessageSender implements Runnable {

    private static final String DISPLAY_CONNECTED_CLIENTS_CMD = "wwhhoo";
    private final Socket socket;
    private final UUID clientUUID;
    private final AtomicBoolean shouldExit;
    private final BufferedReader userInput;
    private final String alias;

    /**
     * Constructs a new MessageSender instance.
     *
     * @param socket       the socket connected to the server
     * @param clientUUID   the unique identifier for the client
     * @param shouldExit   a flag indicating when to stop sending messages
     * @param userInput    the user's input source
     * @param alias        the user's chosen alias
     */
    public MessageSender(Socket socket, UUID clientUUID, AtomicBoolean shouldExit,
                         BufferedReader userInput, String alias) {
        this.socket = socket;
        this.clientUUID = clientUUID;
        this.shouldExit = shouldExit;
        this.userInput = userInput;
        this.alias = alias;
    }

    /**
     * Continuously reads user input and sends messages to the server.
     * The loop stops when the user enters "quit" or when the `shouldExit` flag is set to true.
     */
    @Override
    public void run() {
        try (PrintWriter out = setupPrintWriter()) {

            String line;
            while (!shouldExit.get() && (line = userInput.readLine()) != null) {

                if (line.trim().equalsIgnoreCase("quit")) {
                    shouldExit.set(true);
                    break;
                }

                if (line.equals(DISPLAY_CONNECTED_CLIENTS_CMD)) {
                    out.write(line + "\n");
                } else {
                    String messageWithUUID = clientUUID + "|" + "[" + alias + "]: " + line;
                    out.write(messageWithUUID + "\n");
                }

                out.flush();
            }
        } catch (IOException e) {
            shouldExit.set(true);
        }
    }

    /**
     * Initializes and returns a PrintWriter to send messages to the server.
     *
     * @return PrintWriter for sending messages
     * @throws IOException if an error occurs during PrintWriter setup
     */
    private PrintWriter setupPrintWriter() throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.ISO_8859_1);
        return new PrintWriter(outputStreamWriter, true);
    }
}
