import java.awt.Point;

/**
 * Utility class for converting Point objects to String representations and vice versa.
 */
public class MessageProcessor {

    /**
     * Converts a Point object to its String representation.
     *
     * @param p The Point object to be converted.
     * @return The String representation of the point.
     */
    public static String pointToString(Point p) {
        return p.x + " " + p.y;
    }

    /**
     * Converts a String representation of a point back to a Point object.
     *
     * @param message The String representation of the point.
     * @return The Point object corresponding to the message.
     */
    public static Point stringToPoint(String message) {
        String[] xy = message.split(" ");
        return new Point(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
    }
}
