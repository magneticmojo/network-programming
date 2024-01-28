import java.io.*;
import java.net.*;

/**
 * Client is main thread.
 * */
public class Client {

    // Loop-back address (IPv4)
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 2000;
    private static final int TIME_OUT = 60000;
    private final String host;
    private final int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    // Socket: --> Treat network con as stream onto which bytes can be written/read
    // Client and remote host get in/out streams from socket (full-duplex)
    // Connect to remote machine
    // Send data
    // Receive data
    // Close connection

    private void connect() throws IOException {
        try {
            Socket socket = new Socket(host, port);
            // If server stops communicating with local without closing connection
            socket.setSoTimeout(TIME_OUT);


            // Check if socket is currently open
            boolean connected = socket.isConnected() && ! socket.isClosed();
            if (connected) {

                System.out.println("***********************   SUCCESS   ***********************");
                System.out.println("Socket connection established between local and remote host");
                System.out.println("***********************************************************");
                // Notify user
                InetAddress inetAddress = socket.getInetAddress();
                System.out.println("IP: " + inetAddress.getHostAddress());
                System.out.println("Host name: " + inetAddress.getHostName());
                System.out.println("***********************************************************");

            }

            Thread receiver = new Thread(new MessageReceiver(socket));

            receiver.start();

            Thread sender = new Thread(new MessageSender(socket));

            sender.start();


        } catch (ConnectException e) {
            throw new IOException("ConnectException: Refused remotely for port " + port + " at host " + host, e);
        } catch (SocketTimeoutException e) {
            throw new IOException("SocketTimeoutException: Server at " + host + " hang while connection up", e);
        } catch (NoRouteToHostException e) {
            throw new IOException("NoRouteToHostException: Packet routing to server at " + host + " failed", e);
        } catch (IOException e) {
            throw new IOException("IOException: Connection timed out or failed", e);
        }
    }

    public static void main(String[] args) {
        Client client = initFromArgs(args);
        try {
            client.connect();
        } catch (IOException e) {
            System.err.println("Failed connection: " + e.getMessage());
            System.exit(1);
        }
    }
    private static Client initFromArgs(String[] args) {

        // java Client <host> --> Default port
        if (args.length == 1) {
            System.err.println("host from args");
            return new Client(args[0], DEFAULT_PORT);
        }

        // java Client <host> <port> --> from args
        if (args.length == 2) {
            System.err.println("host and port from args");
            int userSpecifiedPort = 0;
            try {
                userSpecifiedPort = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port number. Using default");
            }

            return new Client(args[0], userSpecifiedPort);
        }

        // java Client --> Default values
        // TODO --> Takes all other than args.length == 1 || 2
        System.err.println("no args || more than two args");
        return new Client(DEFAULT_HOST, DEFAULT_PORT);
    }
}

















































