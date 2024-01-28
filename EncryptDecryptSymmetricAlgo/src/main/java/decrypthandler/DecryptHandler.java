package decrypthandler;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Handles the decryption process by orchestrating file reading and writing,
 * along with the actual decryption of encrypted content.
 */
public class DecryptHandler {

    private final FileService fileService;
    private final DecryptionService decryptionService;

    /**
     * Initializes a new DecryptHandler with the specified file and decryption services.
     *
     * @param fileService Responsible for file operations.
     * @param decryptionService Provides decryption functionality.
     */
    public DecryptHandler(FileService fileService, DecryptionService decryptionService) {
        this.fileService = fileService;
        this.decryptionService = decryptionService;
    }

    /**
     * Decrypts the content of the specified file using the provided key and writes the
     * decrypted content to another file.
     *
     * @param args The command line arguments. Expected to contain:
     *             encryptedDataFile, encodedKey, and decryptedDataFile in that order.
     * @throws Exception if any decryption or file operation fails.
     */
    public synchronized void handleDecryptionRequest(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("Usage: java staticversions.DecryptHandler <encryptedData> <keyAsBase64> <decryptedData>");
            return;
        }

        String encryptedDataFile = args[0], encodedKey = args[1], decryptedDataFile = args[2];

        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        byte[] encryptedBytes = fileService.readAllBytes(encryptedDataFile);
        byte[] decryptedBytes = decryptionService.decryptData(encryptedBytes, originalKey);

        fileService.writeToFile(decryptedDataFile, decryptedBytes);
    }

    /**
     * Entry point of the application, creates a new DecryptHandler instance and
     * processes the command-line arguments for decryption.
     *
     * @param args The command line arguments.
     * @throws Exception if any decryption or file operation fails.
     */
    public static void main(String[] args) throws Exception {
        FileService fileServiceImpl = new FileServiceImpl();
        DecryptionService decryptionServiceImpl = new DecryptionServiceImpl();

        DecryptHandler decryptHandler = new DecryptHandler(fileServiceImpl, decryptionServiceImpl);
        decryptHandler.handleDecryptionRequest(args);
    }
}
