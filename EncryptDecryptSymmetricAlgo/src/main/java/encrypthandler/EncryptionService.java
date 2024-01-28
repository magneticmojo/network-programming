package encrypthandler;

import java.security.GeneralSecurityException;

/**
 * Interface defining an encryption service.
 */
public interface EncryptionService {

    /**
     * Encrypts raw data using a given encoded key.
     *
     * @param data The raw data to be encrypted.
     * @param encodedKey The encoded key for encryption.
     * @return The encrypted byte array.
     * @throws GeneralSecurityException if encryption fails.
     */
    byte[] encryptData(byte[] data, String encodedKey) throws GeneralSecurityException;
}
