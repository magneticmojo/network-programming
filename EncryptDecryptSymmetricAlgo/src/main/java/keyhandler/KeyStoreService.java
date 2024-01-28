package keyhandler;

import javax.crypto.SecretKey;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

/**
 * Service to manage various operations related to a KeyStore. This includes generating
 * secret keys, saving and retrieving keys from a KeyStore, and more.
 */
public interface KeyStoreService {

    /**
     * Generates a SecretKey based on a specified algorithm and key size.
     *
     * @return A SecretKey object generated based on the criteria.
     * @throws NoSuchAlgorithmException If the algorithm to generate the key is not found.
     */
    SecretKey generateSecretKey() throws NoSuchAlgorithmException;

    /**
     * Saves the given secret key to the specified KeyStore at the provided path.
     *
     * @param secretKey     The secret key to be stored.
     * @param keyStorePath  The path where the KeyStore is located.
     * @param keyAlias      The alias for the secret key.
     * @param password      The password to secure the KeyStore.
     * @throws Exception If any issues occur while saving the key to the KeyStore.
     */
    void saveKeyToKeyStore(SecretKey secretKey, String keyStorePath, String keyAlias, char[] password) throws Exception;

    /**
     * Retrieves a SecretKey from a KeyStore given its alias.
     *
     * @param keyAlias      The alias of the key.
     * @param keyStorePath  The path where the KeyStore is located.
     * @param password      The password used to secure the KeyStore.
     * @return The requested SecretKey.
     * @throws Exception If any issues occur while retrieving the key from the KeyStore.
     */
    SecretKey getKeyFromKeyStore(String keyAlias, String keyStorePath, char[] password) throws Exception;

    /**
     * Removes a key from the KeyStore using the provided alias.
     *
     * @param keyAlias      The alias of the key to be removed.
     * @param keyStorePath  The path where the KeyStore is located.
     * @param password      The password used to secure the KeyStore.
     * @throws Exception If any issues occur while removing the key from the KeyStore.
     */
    void removeKeyFromKeyStore(String keyAlias, String keyStorePath, char[] password) throws Exception;

    /**
     * Displays the aliases of all keys present in the specified KeyStore.
     *
     * @param keyStorePath  The path where the KeyStore is located.
     * @param password      The password used to secure the KeyStore.
     * @throws Exception If any issues occur while displaying the keys.
     */
    void displayKeysInKeyStore(String keyStorePath, char[] password) throws Exception;

    /**
     * Creates a new KeyStore at the specified path.
     *
     * @param keyStorePath  The path where the KeyStore should be created.
     * @param password      The password to secure the newly created KeyStore.
     * @throws Exception If any issues occur while creating the KeyStore.
     */
    void createKeyStore(String keyStorePath, char[] password) throws Exception;

    /**
     * Loads an existing KeyStore from the specified path.
     *
     * @param keyStorePath  The path where the KeyStore is located.
     * @param password      The password used to secure the KeyStore.
     * @return A loaded KeyStore object.
     * @throws Exception If any issues occur while loading the KeyStore.
     */
    KeyStore loadExistingKeyStore(String keyStorePath, char[] password) throws Exception;
}
