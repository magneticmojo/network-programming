import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientWithThreads {

    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 2000;
    private static final int TIME_OUT = 60000;
    private static final String DISPLAY_CONNECTED_CLIENTS_CMD = "wwhhoo";
    private static final String DEFAULT_ALIAS = "No_ Nick_";

    private final String host;
    private final int port;
    private final String alias;
    private volatile boolean shouldExit;
    private final ConcurrentMap<UUID, String> sentMessages = new ConcurrentHashMap<>();

    public ClientWithThreads(String host, int port, String alias) {
        this.host = host;
        this.port = port;
        this.alias = alias;
    }

    public static void main(String[] args) {
        String alias = setUserAlias();
        ClientWithThreads client = initFromArgs(args, alias);
        try {
            client.connect();
        } catch (IOException e) {
            System.err.println("Connection ended in catch main: " + e.getMessage());
        } finally {
            System.exit(1);
        }
    }

    private static String setUserAlias() {
        System.out.println("Enter your alias:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String alias;
        try {
            alias = reader.readLine();
            if (alias == null || alias.isEmpty() || alias.isBlank()) {
                alias = DEFAULT_ALIAS + new Random().nextInt(10000);
            }
        } catch (IOException e) {
            alias = DEFAULT_ALIAS + new Random().nextInt(10000);
        }
        return alias;
    }

    private static ClientWithThreads initFromArgs(String[] args, String alias) {

        if (args.length == 1) {
            return new ClientWithThreads(args[0], DEFAULT_PORT, alias);
        } else if (args.length == 2) {
            int userSpecifiedPort = 0;
            try {
                userSpecifiedPort = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port number. Using default");
            }
            return new ClientWithThreads(args[0], userSpecifiedPort, alias);
        }
        return new ClientWithThreads(DEFAULT_HOST, DEFAULT_PORT, alias);
    }

    private void connect() throws IOException {
        Thread receiver;
        Thread sender;

        try (Socket socket = new Socket(host, port)) {
            // If server stops communicating with local without closing connection
            socket.setSoTimeout(TIME_OUT);

            // Check if socket is currently open
            boolean connected = socket.isConnected() && !socket.isClosed(); // explain why both calls are needed?
            if (connected) {
                // Notify user that connection succeeded + host and port
                printConnectionDetailsToUser(socket);
            }

            receiver = new Thread(new MessageReceiver(socket));

            receiver.start();

            sender = new Thread(new MessageSender(socket));

            sender.start();

            // I did this so that the main thread could exit with system.exit --> thinking this would be a graceful shutdown
            receiver.join();
            sender.join();

        } catch (ConnectException e) {
            throw new IOException("ConnectException: Refused remotely for port " + port + " at host " + host, e);
        } catch (SocketTimeoutException e) {
            throw new IOException("SocketTimeoutException: Server at " + host + " hang while connection up", e);
        } catch (NoRouteToHostException e) {
            throw new IOException("NoRouteToHostException: Packet routing to server at " + host + " failed", e);
        } catch (IOException e) {
            throw new IOException("IOException: Connection timed out or failed", e);
        } catch (InterruptedException e) {
            throw new IOException("Thread interrupted", e);
        } catch (Exception e) {
            throw new IOException("Exception: " + e.getMessage(), e);
        }
    }

    private static void printConnectionDetailsToUser(Socket socket) {
        String separatorLine = String.format("%1$-60s", "").replace(' ', '*');
        InetAddress inetAddress = socket.getInetAddress();

        String ipLine = String.format("IP: %s", inetAddress.getHostAddress());
        String hostNameLine = String.format("Host name: %s", inetAddress.getHostName());

        System.out.println("***********************   SUCCESS   ***********************");
        System.out.println("Socket connection established between local and remote host");
        System.out.println(separatorLine);
        System.out.println(ipLine);
        System.out.println(hostNameLine);
        System.out.println(separatorLine);
        System.out.println("Enter 'quit' to exit");
        System.out.println(separatorLine);
    }

    private class MessageSender implements Runnable {

        private final Socket socket;

        public MessageSender(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.ISO_8859_1), true)) {
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

                String line;
                while (!shouldExit && (line = userInput.readLine()) != null) {

                    UUID messageID = UUID.randomUUID();
                    sentMessages.put(messageID, line);

                    String messageWithUUID = messageID + "|" + "[" + alias + "]: " + line;

                    if (line.trim().equalsIgnoreCase("quit")) {
                        // Notify MessageReceiver thread to stop execution
                        shouldExit = true;
                        break;
                    }

                    if (line.equals(DISPLAY_CONNECTED_CLIENTS_CMD)) {
                        out.write(line + "\n");
                    } else {
                        out.write(messageWithUUID + "\n");
                    }

                    out.flush();
                }
            } catch (IOException e) {
                shouldExit = true;
            }
        }
    }

    private class MessageReceiver implements Runnable {

        private final Socket socket;

        public MessageReceiver(Socket socket) {
            this.socket = socket;

        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                String uuidRegex = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
                Pattern uuidPattern = Pattern.compile(uuidRegex);

                while (!shouldExit) {
                    String line;

                    line = in.readLine();
                    if (line == null) {
                        // Server closed connection
                        System.err.println("Connection failure, quit and retry with another port");
                        shouldExit = true;
                        break;
                    }

                    // Use regex to check if line starts with a UUID.
                    Matcher matcher = uuidPattern.matcher(line);
                    if (matcher.find()) {
                        // Extract UUID and check in the map.
                        String[] parts = line.split("\\|", 2);
                        UUID messageId = UUID.fromString(parts[0]); // UUID is at the beginning

                        if (sentMessages.containsKey(messageId)) {
                            sentMessages.remove(messageId);
                        } else {
                            System.out.println(parts[1]);
                        }
                    } else {
                        // The line does not start with a UUID, handle it accordingly.
                        System.out.println(line);
                    }
                }
            } catch (SocketTimeoutException ste) {
                System.err.println("Socket timeout. Terminating connection");
                shouldExit = true;
                try {
                    socket.close();
                } catch (IOException e) {
                    // Ignore
                }
            } catch (IOException e) {
                shouldExit = true;
            }
        }
    }
}

