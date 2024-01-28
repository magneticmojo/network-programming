package decrypthandler;

import javax.crypto.SecretKey;

/**
 * Represents a service that provides decryption functionality.
 */
public interface DecryptionService {

    /**
     * Decrypts the provided encrypted data using the specified key.
     *
     * @param encryptedData The encrypted data to be decrypted.
     * @param key The decryption key.
     * @return The decrypted data.
     * @throws Exception if the decryption fails.
     */
    byte[] decryptData(byte[] encryptedData, SecretKey key) throws Exception;
}



