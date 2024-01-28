import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MessageSender implements Runnable {

    private final Socket socket;

    public MessageSender(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.ISO_8859_1), true)) {
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            String line;
            while ((line = userInput.readLine()) != null) {
                out.write(line + "\n");
                out.flush();
                if (line.trim().equalsIgnoreCase("quit")) {
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Exception in MessageSender thread");
        }
    }
}
