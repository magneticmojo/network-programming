package staticversions;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.*;
import java.util.Base64;

public class DecryptHandler {
    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("Usage: java staticversions.DecryptHandler <encryptedData> <keyAsBase64> <decryptedData>");
            return;
        }

        String encryptedDataFile = args[0], encodedKey = args[1], decryptedDataFile = args[2];

        // Decode the key from Base64
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        // Decryption
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, originalKey);
        byte[] encryptedBytes = Files.readAllBytes(Paths.get(encryptedDataFile));
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        // Check if the decrypted data file exists
        Path decryptedDataPath = Paths.get(decryptedDataFile);
        if (!Files.exists(decryptedDataPath)) {
            // If the file doesn't exist, create it
            Files.createFile(decryptedDataPath);
            System.out.println("The file did not exist. It has been created at: " + decryptedDataFile);
        }

        // Write decrypted data to the file
        Files.write(decryptedDataPath, decryptedBytes);
        System.out.println("Decrypted data has been written to: " + decryptedDataFile);
    }
}
