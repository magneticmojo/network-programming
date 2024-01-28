import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;

/**
 * Represents the drawing canvas, allowing users to draw points using the mouse.
 * It also communicates drawing actions to the UDP manager to send across the network.
 */
public class Paper extends JPanel {

    private final HashSet<Point> points = new HashSet<>();
    private UDPManager udpManager;

    /**
     * Initializes a new Paper with required event listeners for drawing.
     */
    public Paper() {
        setBackground(Color.white);
        addMouseListener(new L1());
        addMouseMotionListener(new L2());
    }

    /**
     * Assigns a UDP manager to the paper for communication.
     *
     * @param manager The UDPManager instance for managing messages.
     */
    public void setUDPManager(UDPManager manager) {
        this.udpManager = manager;
    }

    /**
     * Draws the points on the canvas.
     *
     * @param g The Graphics object used for drawing on the component.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        for(Point p : points) {
            g.fillOval(p.x, p.y, 2, 2);
        }
    }

    /**
     * Adds a point to the canvas, typically received from a remote source.
     *
     * @param p The point to be added.
     */
    public void externallyAddPoint(Point p) {
        points.add(p);
        repaint();
    }

    /**
     * Adds a point to the canvas and notifies the UDP manager to send the point.
     *
     * @param p The point to be added.
     */
    private void addPoint(Point p) {
        points.add(p);
        udpManager.sendMessage(p);
        repaint();
    }

    /**
     * An adapter class to handle mouse pressed events.
     * When the mouse is pressed on the canvas, the corresponding point is added.
     */
    class L1 extends MouseAdapter {
        public void mousePressed(MouseEvent me) {
            addPoint(me.getPoint());
        }
    }

    /**
     * An adapter class to handle mouse dragged events.
     * As the mouse is dragged on the canvas, the points are continuously added.
     */
    class L2 extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent me) {
            addPoint(me.getPoint());
        }
    }
}
