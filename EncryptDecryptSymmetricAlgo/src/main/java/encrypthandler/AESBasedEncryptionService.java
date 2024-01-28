package encrypthandler;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Base64;

/**
 * Implementation of the {@link EncryptionService} that leverages AES encryption.
 */
public class AESBasedEncryptionService implements EncryptionService {

    /**
     * Encrypts the given data using AES encryption.
     *
     * @param data The raw data to be encrypted.
     * @param encodedKey The Base64 encoded AES key for encryption.
     * @return The encrypted byte array.
     * @throws GeneralSecurityException if encryption fails.
     */
    @Override
    public byte[] encryptData(byte[] data, String encodedKey) throws GeneralSecurityException {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, originalKey);
        return cipher.doFinal(data);
    }
}
