package encrypthandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Implementation of the {@link FileService} for default file operations.
 */
public class DefaultFileService implements FileService {

    /**
     * Reads the content of a file located at the given path into a byte array.
     *
     * @param path The path to the file.
     * @return The content of the file as a byte array.
     * @throws IOException if reading from the file fails.
     */
    @Override
    public byte[] readAllBytes(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }

    /**
     * Writes the provided byte data to a file located at the specified path.
     * Notifies the user once the data has been successfully written.
     *
     * @param path The path to the file.
     * @param data The byte array data to be written.
     * @throws IOException if writing to the file fails.
     */
    @Override
    public void writeToFile(String path, byte[] data) throws IOException {
        Files.write(Paths.get(path), data);
        System.out.println("Data has been written to: " + path);
    }
}