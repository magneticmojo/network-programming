package staticversions;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.*;
import java.util.Base64;

public class EncryptHandler {

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("Usage: java staticversions.EncryptHandler <data> <keyAsBase64> <encryptedData>");
            return;
        }

        String dataFile = args[0], encodedKey = args[1], encryptedDataFile = args[2];

        // Decode the key from Base64
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        // Encryption
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, originalKey);
        byte[] dataBytes = Files.readAllBytes(Paths.get(dataFile));
        byte[] encryptedBytes = cipher.doFinal(dataBytes);

        // Check if the file exists
        Path encryptedDataPath = Paths.get(encryptedDataFile);
        if (!Files.exists(encryptedDataPath)) {
            // If the file doesn't exist, create it
            Files.createFile(encryptedDataPath);
            System.out.println("The file did not exist. It has been created at: " + encryptedDataFile);
        }

        // Write encrypted data to the file
        Files.write(encryptedDataPath, encryptedBytes);
        System.out.println("Encrypted data has been written to: " + encryptedDataFile);
    }
}
