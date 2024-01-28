import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Represents a handler for individual client connections.
 * Manages the input and output streams for a client, reads messages from the client,
 * and sends messages to the client. When a client sends a message, it is forwarded to
 * the server for broadcasting.
 */
public class ClientHandler implements Runnable {

    private final Socket connection;
    private final Server server;
    private final PrintWriter out;
    private final BufferedReader in;

    /**
     * Constructs a new ClientHandler for a given client connection and server.
     *
     * @param clientConnection The socket representing the client connection.
     * @param server           The chat server to which this handler belongs.
     * @throws IOException If there's an issue setting up the input or output streams.
     */
    public ClientHandler(Socket clientConnection, Server server) throws IOException {
        this.connection = clientConnection;
        this.server = server;
        this.in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        this.out = new PrintWriter(connection.getOutputStream(), true);
    }

    /**
     * Returns the IP address of the connected client.
     *
     * @return The IP address as a string.
     */
    public String getClientIP() {
        return connection.getInetAddress().getHostAddress();
    }

    /**
     * Main logic for handling client messages. Reads messages from the client and forwards
     * them to the server for broadcasting. If a client sends a "quit" message, the connection is terminated.
     */
    @Override
    public void run() {
        try {
            String message;
            while ((message = in.readLine()) != null) {

                if (message.equals("quit")) {
                    break;
                }

                server.broadcastMessage(message, getClientIP());

            }
        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                closeAllResources();
            } catch (IOException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
            server.removeClient(this);
        }
    }

    /**
     * Closes all resources associated with this client handler including the input and output streams.
     *
     * @throws IOException If there's an issue closing any of the resources.
     */
    private void closeAllResources() throws IOException {
        if (out != null) {
            out.close();
        }
        if (in != null) {
            in.close();
        }
        connection.close();
    }

    /**
     * Sends a message to the client associated with this handler.
     * The method is called by the Server instance.
     *
     * @param message The message to be sent.
     */
    public void sendMessage(String message) {
        out.println(message);
    }
}
