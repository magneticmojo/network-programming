/**
 * Custom exception to indicate an invalid email format.
 */
public class InvalidEmailException extends Exception {
    public InvalidEmailException(String message) {
        super(message);
    }
}
