import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * Represents the main drawing frame for the application.
 * It initializes the paper for drawing and manages the UDP communication.
 * It also ensures that the UDP resources are cleaned up upon window closing.
 */
public class DrawFrame extends JFrame {

    private final UDPManager udpManager;

    /**
     * Constructs the drawing frame and initializes the UDP manager.
     *
     * @param localPort   The local port for receiving messages.
     * @param remoteHost  The remote host to send messages to.
     * @param remotePort  The remote port to send messages to.
     * @throws Exception If any initialization error occurs.
     */
    public DrawFrame(int localPort, String remoteHost, int remotePort) throws Exception {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Paper paper = new Paper();
        getContentPane().add(paper, BorderLayout.CENTER);

        udpManager = new UDPManager(localPort, remoteHost, remotePort, paper);
        udpManager.startReceiver();

        paper.setUDPManager(udpManager);

        setSize(640, 480);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                udpManager.cleanupResources();
            }
        });
    }

    /**
     * The main entry point of the DrawFrame application.
     *
     * @param args Command-line arguments, expects local port, remote host, and remote port.
     * @throws Exception If any initialization error occurs.
     */
    public static void main(String[] args) throws Exception {
        if(args.length < 3) {
            System.out.println("Usage: java Draw <my port> <remote host> <remote port>");
            return;
        }
        new DrawFrame(Integer.parseInt(args[0]), args[1], Integer.parseInt(args[2]));
    }
}
