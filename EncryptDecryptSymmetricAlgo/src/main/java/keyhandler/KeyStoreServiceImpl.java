package keyhandler;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Implementation of {@link KeyStoreService}. Provides functionality to manage
 * keys in a PKCS12 KeyStore.
 */
public class KeyStoreServiceImpl implements KeyStoreService {
    private static final int BITS_KEY_SIZE = 256;
    private static final String CRYPTOGRAPHIC_SYMMETRIC_ALGORITHM = "AES";
    private static final String PKCS12_FILE_EXT = ".p12";
    private static final String KEYSTORE_TYPE = "PKCS12";

    /**
     * Generates a symmetric secret key based on the AES algorithm and given key size.
     *
     * @return a newly generated secret key.
     * @throws NoSuchAlgorithmException if the AES algorithm is not supported by the JVM.
     */
    @Override
    public SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(CRYPTOGRAPHIC_SYMMETRIC_ALGORITHM);
        keyGen.init(BITS_KEY_SIZE);
        return keyGen.generateKey();
    }

    /**
     * Saves the provided secret key into a PKCS12 KeyStore located at the given path.
     *
     * @param secretKey    the secret key to be stored.
     * @param keyStorePath the path of the KeyStore.
     * @param keyAlias     the alias name for the secret key.
     * @param password     the password to access the KeyStore.
     * @throws Exception if there are issues loading or storing the KeyStore.
     */
    @Override
    public void saveKeyToKeyStore(SecretKey secretKey, String keyStorePath, String keyAlias, char[] password) throws Exception {
        KeyStore keyStore = loadExistingKeyStore(keyStorePath, password);

        // Check if the provided keyAlias already exists
        if (keyStore.containsAlias(keyAlias)) {
            System.out.println("Error: The keyAlias '" + keyAlias + "' already exists in the keystore.");
            return;
        }

        KeyStore.SecretKeyEntry keyEntry = new KeyStore.SecretKeyEntry(secretKey);
        keyStore.setEntry(keyAlias, keyEntry, new KeyStore.PasswordProtection(password));

        storeKeyStoreToFile(keyStore, keyStorePath, password);

        System.out.println("Key added to keystore under the alias '" + keyAlias + "'.");
    }

    /**
     * Internal helper to persist the given KeyStore to a file.
     *
     * @param keyStore     the KeyStore to be saved.
     * @param keyStorePath the path to save the KeyStore.
     * @param password     the password to protect the KeyStore.
     * @throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException if there's an error while writing the KeyStore.
     */
    private void storeKeyStoreToFile(KeyStore keyStore, String keyStorePath, char[] password) throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException {
        try (OutputStream fos = new FileOutputStream(keyStorePath)) {
            keyStore.store(fos, password);
        }
    }

    /**
     * Retrieves a secret key from the KeyStore using the provided alias.
     *
     * @param keyAlias     the alias of the desired secret key.
     * @param keyStorePath the path to the KeyStore.
     * @param password     the password to access the KeyStore.
     * @return the secret key associated with the given alias.
     * @throws Exception if the key cannot be retrieved.
     */
    @Override
    public SecretKey getKeyFromKeyStore(String keyAlias, String keyStorePath, char[] password) throws Exception {
        KeyStore keyStore = loadExistingKeyStore(keyStorePath, password);

        if (!keyStore.containsAlias(keyAlias)) {
            throw new KeyStoreException("Key alias not found.");
        }

        return (SecretKey) keyStore.getKey(keyAlias, password);
    }

    /**
     * Removes a key from the KeyStore associated with the provided alias.
     *
     * @param keyAlias     the alias of the key to be removed.
     * @param keyStorePath the path to the KeyStore.
     * @param password     the password to access the KeyStore.
     * @throws Exception if the key cannot be removed.
     */
    @Override
    public void removeKeyFromKeyStore(String keyAlias, String keyStorePath, char[] password) throws Exception {
        KeyStore keyStore = loadExistingKeyStore(keyStorePath, password);
        if (!keyStore.containsAlias(keyAlias)) {
            System.out.println("Key alias not found.");
            return;
        }

        keyStore.deleteEntry(keyAlias);

        storeKeyStoreToFile(keyStore, keyStorePath, password);

        System.out.println("Key with alias " + keyAlias + " removed successfully.");
    }

    /**
     * Displays all key aliases stored in the specified KeyStore.
     *
     * @param keyStorePath the path to the KeyStore.
     * @param password     the password to access the KeyStore.
     * @throws Exception if there's an error while accessing the KeyStore.
     */
    @Override
    public void displayKeysInKeyStore(String keyStorePath, char[] password) throws Exception {
        KeyStore keyStore = loadExistingKeyStore(keyStorePath, password);

        Enumeration<String> aliases = keyStore.aliases();
        List<String> aliasList = Collections.list(aliases);  // Convert Enumeration to List to check if empty

        if (aliasList.isEmpty()) {
            System.out.println("No keys present in keystore");
        } else {
            for (String alias : aliasList) {
                System.out.println(alias);
            }
        }
    }

    /**
     * Creates a new empty PKCS12 KeyStore at the provided path.
     *
     * @param keyStorePath the path to create the new KeyStore.
     * @param password     the password to protect the new KeyStore.
     * @throws Exception if the KeyStore cannot be created.
     */
    @Override
    public void createKeyStore(String keyStorePath, char[] password) throws Exception {
        // Check if the provided file path has the .p12 extension
        if (!keyStorePath.endsWith(PKCS12_FILE_EXT)) {
            System.out.println("Error: The keystore file path must have a '" + PKCS12_FILE_EXT + "' extension.");
            return;
        }

        KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
        File keystoreFile = new File(keyStorePath);
        File keyStoreDirectory = keystoreFile.getParentFile(); // Extract the directory of the keystore

        // Check if the directory exists
        if (!keyStoreDirectory.exists()) {
            System.out.println("Error: The specified directory does not exist.");
            return;
        }

        // Check if it's actually a directory
        if (!keyStoreDirectory.isDirectory()) {
            System.out.println("Error: The specified path for the directory is not valid.");
            return;
        }

        // Check if the keystore file already exists
        if (keystoreFile.exists()) {
            System.out.println("Error: A keystore file already exists at the specified path.");
            return;
        }

        keyStore.load(null, password); // Initializing an empty keystore

        storeKeyStoreToFile(keyStore, keyStorePath, password);

        System.out.println("KeyStore created successfully at " + keystoreFile.getAbsolutePath());
    }

    /**
     * Loads a PKCS12 KeyStore located at the provided path.
     *
     * @param keyStorePath the path to the KeyStore.
     * @param password     the password to access the KeyStore.
     * @return the loaded KeyStore.
     * @throws Exception if the KeyStore cannot be loaded.
     */
    @Override
    public KeyStore loadExistingKeyStore(String keyStorePath, char[] password) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
        File keystoreFile = new File(keyStorePath);

        // Check if keystore exists
        if (!keystoreFile.exists()) {
            throw new FileNotFoundException("Error: The specified keystore does not exist.");
        }

        // Check if the provided path is a directory
        if (keystoreFile.isDirectory()) {
            throw new IllegalArgumentException("Error: The specified keystore path points to a directory, not a file.");
        }

        // Check read permissions on keystore
        if (!keystoreFile.canRead()) {
            throw new SecurityException("Error: Cannot read the specified keystore.");
        }

        // Load the keystore
        try (InputStream fis = new FileInputStream(keyStorePath)) {
            keyStore.load(fis, password);
        }

        return keyStore;
    }
}
