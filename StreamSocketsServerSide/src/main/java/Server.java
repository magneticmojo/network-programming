import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Represents the chat server which manages multiple client connections.
 * It accepts connections from clients, broadcasts messages to all connected clients,
 * and maintains the list of connected clients.
 */
public class Server {

    private static final int DEFAULT_PORT = 2000;
    private final Queue<ClientHandler> clients;
    private final ServerSocket serverSocket;
    private final String serverHost;
    private final int serverPort;

    public Server(int port) throws IOException {
        this.clients = new ConcurrentLinkedQueue<>();
        this.serverSocket = new ServerSocket(port);
        this.serverHost = serverSocket.getInetAddress().getHostAddress();
        this.serverPort = serverSocket.getLocalPort();
    }

    /**
     * Initiates the server and listens for client connections on the given port.
     */
    public void run() {

        System.out.println("*******************>>>>> Chat Server Up And Running <<<<<*******************");

        try (serverSocket) {
            while (true) {
                try {
                    Socket clientConnection = serverSocket.accept();

                    ClientHandler clientHandler = new ClientHandler(clientConnection, this);
                    clients.add(clientHandler);

                    Thread clientThread = new Thread(clientHandler);
                    clientThread.start();

                    printServerInfo();

                    broadcastClientConnectionStatus("CLIENT CONNECTED: ", clientHandler);

                } catch (IOException e) {
                    System.out.println("Issue with a client connection: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Server shutdown: " + e.getMessage());
        }
    }

    /**
     * Prints the current state of the server including its IP, port, and number of connected clients.
     */
    private void printServerInfo() {
        System.out.println("Server running on host " + serverHost + " on port " + serverPort + " with " + clients.size() + " clients connected.");
    }

    /**
     * Broadcasts a message to all clients except the sender.
     *
     * @param message   The message to be broadcast.
     * @param senderIP  The IP address of the sending client to avoid sending back to the sender.
     */
    public synchronized void broadcastMessage(String message, String senderIP) {

        System.out.println(senderIP + ": " + message);

        for (ClientHandler client : clients) {
            if (!client.getClientIP().equals(senderIP)) {

                client.sendMessage(senderIP + ": " + message);
            }
        }
    }

    /**
     * Broadcasts client connection or disconnection status to all other clients.
     *
     * @param message            The message to be broadcast.
     * @param clientHandler  The client handler for the client whose status changed.
     */
    private synchronized void broadcastClientConnectionStatus(String message, ClientHandler clientHandler) {
        String clientIP = clientHandler.getClientIP();

        for (ClientHandler client : clients) {
            if (!client.getClientIP().equals(clientIP)) {
                client.sendMessage(message + clientIP);
            }
        }
    }

    /**
     * Removes a client from the list of connected clients and notifies others of the disconnection.
     *
     * @param clientHandler The client handler representing the client to be removed.
     */
    public void removeClient(ClientHandler clientHandler) {
        broadcastClientConnectionStatus("CLIENT DISCONNECTED: ", clientHandler);
        clients.remove(clientHandler);
        printServerInfo();
    }

    /**
     * The main entry point for the Server application. Initiates the server on a given port or the default port.
     *
     * @param args Command-line arguments, expects the port number as the first argument.
     */
    public static void main(String[] args) throws IOException {
        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (RuntimeException e) {
            port = DEFAULT_PORT;
        }

        Server server = new Server(port);
        server.run();
    }
}
