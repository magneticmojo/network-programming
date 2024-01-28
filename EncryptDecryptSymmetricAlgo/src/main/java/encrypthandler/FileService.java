package encrypthandler;

import java.io.IOException;

/**
 * Interface defining methods for file operations.
 */
public interface FileService {

    /**
     * Reads all bytes from the file located at the given path.
     *
     * @param path The path to the file.
     * @return The content of the file as a byte array.
     * @throws IOException if reading from the file fails.
     */
    byte[] readAllBytes(String path) throws IOException;
    /**
     * Writes data to a file located at the specified path.
     *
     * @param path The path to the file.
     * @param data The byte array data to be written.
     * @throws IOException if writing to the file fails.
     */
    void writeToFile(String path, byte[] data) throws IOException;
}
