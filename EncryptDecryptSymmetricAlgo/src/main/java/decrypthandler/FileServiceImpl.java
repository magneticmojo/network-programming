package decrypthandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Default implementation of the {@link FileService} for basic file operations.
 */
public class FileServiceImpl implements FileService {

    /**
     * Reads all the bytes from the file located at the specified path.
     *
     * @param path The path of the file.
     * @return An array of bytes read from the file.
     * @throws IOException if there's an error during reading.
     */
    @Override
    public byte[] readAllBytes(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }

    /**
     * Writes the provided byte data to the file located at the specified path.
     * If the file does not exist, it's created. Informs the user about the success
     * of the operation.
     *
     * @param path The path to the file.
     * @param data The byte data to write.
     * @throws IOException if the write operation fails.
     */
    @Override
    public void writeToFile(String path, byte[] data) throws IOException {
        Path filePath = Paths.get(path);
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
            System.out.println("The file did not exist. It has been created at: " + path);
        }
        Files.write(filePath, data);
        System.out.println("Data has been written to: " + path);
    }
}
