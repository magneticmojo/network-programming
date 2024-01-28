import javax.swing.*;
import java.awt.*;
import java.net.*;

/**
 * Manages the UDP communication for sending and receiving drawing points.
 * Also manages a separate thread for receiving incoming messages.
 */
public class UDPManager {
    private final DatagramSocket socket;
    private final InetAddress address;
    private final int port;
    private final Paper paper;

    /**
     * Constructs a new UDPManager with specified communication parameters.
     *
     * @param localPort   The local port for receiving messages.
     * @param remoteHost  The remote host to send messages to.
     * @param remotePort  The remote port to send messages to.
     * @param paper       The Paper instance for updating drawings.
     * @throws Exception If any initialization error occurs.
     */
    public UDPManager(int localPort, String remoteHost, int remotePort, Paper paper) throws Exception {
        this.paper = paper;
        this.socket = new DatagramSocket(localPort);
        this.address = InetAddress.getByName(remoteHost);
        this.port = remotePort;
    }

    /**
     * Starts the UDPReceiver thread for receiving messages.
     */
    public void startReceiver() {
        new Thread(new UDPReceiver()).start();
    }

    /**
     * Sends a point message to the remote host using UDP.
     *
     * @param p The point to be sent.
     */
    public void sendMessage(Point p) {
        String message = MessageProcessor.pointToString(p);
        byte[] buf = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        try {
            socket.send(packet);
        } catch (Exception ex) {
            System.out.println("Error while sending packet: " + ex.getMessage());
        }
    }

    /**
     * Cleans up UDP resources, specifically closes the socket if open.
     */
    public void cleanupResources() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    /**
     * The {@code UDPReceiver} class serves as a dedicated listener to continuously receive UDP packets.
     * Each received packet contains information about a drawing point which is then processed and added
     * to the {@link Paper} canvas.
     *
     * <p>This class implements the {@link Runnable} interface, allowing it to be executed in a separate
     * thread. The {@code run()} method contains the main logic for listening and processing incoming UDP
     * packets.
     *
     * <p>On receiving a packet, it does the following:
     * <ol>
     *     <li>Decodes the packet content into a {@link String} representation.</li>
     *     <li>Converts the string into a {@link Point} using the {@link MessageProcessor} utility.</li>
     *     <li>Invokes the {@code externallyAddPoint} method of the {@link Paper} class using the Swing thread
     *         to ensure thread safety in GUI operations.</li>
     * </ol>
     *
     * <p>Note: Any errors during packet reception or processing are printed to the console.
     */
    private class UDPReceiver implements Runnable {

        /**
         * The main logic for continuously listening and processing incoming UDP packets.
         *
         * <p>It initializes a byte buffer and a {@link DatagramPacket} to receive data. A while loop ensures
         * continuous listening. For each packet received, the method decodes the content, converts it to a
         * {@link Point}, and updates the {@link Paper} canvas.
         */
        @Override
        public void run() {
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            while (true) {
                try {
                    socket.receive(packet);
                    String received = new String(packet.getData(), 0, packet.getLength());
                    Point p = MessageProcessor.stringToPoint(received);
                    SwingUtilities.invokeLater(() -> paper.externallyAddPoint(p));
                } catch (Exception ex) {
                    System.out.println("Error while receiving packet: " + ex.getMessage());
                }
            }
        }
    }
}
