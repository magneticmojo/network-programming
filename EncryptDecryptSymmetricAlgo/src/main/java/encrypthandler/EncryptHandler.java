package encrypthandler;

/**
 * Handles the encryption process by orchestrating the file reading and writing,
 * along with the actual encryption.
 */
public class EncryptHandler {

    private final EncryptionService encryptionService;
    private final FileService fileService;

    /**
     * Constructs a new EncryptHandler with specified encryption and file services.
     *
     * @param encryptionService The encryption service to be used for encryption.
     * @param fileService The file service for reading and writing files.
     */
    public EncryptHandler(EncryptionService encryptionService, FileService fileService) {
        this.encryptionService = encryptionService;
        this.fileService = fileService;
    }

    /**
     * Processes the request by reading the data file, encrypting its contents and then writing
     * the encrypted data to a new file.
     *
     * @param args The command line arguments. Expected to contain:
     *             dataFile, encodedKey, and encryptedDataFile in that order.
     * @throws Exception if any step in the process fails.
     */
    public void processRequest(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("Usage: java staticversions.EncryptHandler <data> <keyAsBase64> <encryptedData>");
            return;
        }

        String dataFile = args[0], encodedKey = args[1], encryptedDataFile = args[2];
        byte[] dataBytes = fileService.readAllBytes(dataFile);
        byte[] encryptedBytes = encryptionService.encryptData(dataBytes, encodedKey);
        fileService.writeToFile(encryptedDataFile, encryptedBytes);
    }

    /**
     * The entry point for the application. It initializes the EncryptHandler with the appropriate
     * services and processes the request based on command line arguments.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        EncryptHandler handler = new EncryptHandler(new AESBasedEncryptionService(), new DefaultFileService());

        try {
            handler.processRequest(args);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
