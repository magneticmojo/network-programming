package decrypthandler;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

/**
 * Default implementation of the {@link DecryptionService}.
 */
public class DecryptionServiceImpl implements DecryptionService {

    /**
     * Decrypts the provided encrypted data using the specified key.
     *
     * @param encryptedData The encrypted data to be decrypted.
     * @param key The decryption key.
     * @return The decrypted data.
     * @throws Exception if the decryption process encounters an issue.
     */
    @Override
    public byte[] decryptData(byte[] encryptedData, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(encryptedData);
    }
}
