import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class MessageReceiver implements Runnable {

    private final Socket socket;

    // TODO -> Who should handle closing the socket???

    public MessageReceiver(Socket socket) {
        this.socket = socket;

    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while (true) {
                String line;
                try {
                    line = in.readLine();
                    if (line == null) {
                        // Server closed connection
                        System.err.println("Server closed the connection");
                        break;
                    }
                    System.out.println("received: " + line);
                } catch (SocketException e) {
                    System.err.println("Socket closed, possibly due to a disconnect");
                    break;
                }
                Thread.sleep(1000);
            }
        } catch (IOException e) {
            System.err.println("Message receiver IOException");
        } catch (InterruptedException e) {
            // TODO --> ignore ok?
        }

    }
}
