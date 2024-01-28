package decrypthandler;

import java.io.IOException;

/**
 * Interface for basic file operations like reading and writing bytes.
 */
public interface FileService {

    /**
     * Reads all the bytes from the file located at the specified path.
     *
     * @param path The path of the file.
     * @return An array of bytes read from the file.
     * @throws IOException if there's an error during reading.
     */
    byte[] readAllBytes(String path) throws IOException;

    /**
     * Writes the specified byte data to the file located at the given path.
     *
     * @param path The path to the file.
     * @param data The data to be written.
     * @throws IOException if there's an error during writing.
     */
    void writeToFile(String path, byte[] data) throws IOException;
}
