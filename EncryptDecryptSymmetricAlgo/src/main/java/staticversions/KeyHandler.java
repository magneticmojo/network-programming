package staticversions;

import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.*;
import javax.crypto.*;

// TODO -> FIX EXCEPTION HANDLING
// TODO -> FIX CONCURRENCY
public class KeyHandler {

    private static final String CRYPTOGRAPHIC_SYMMETRIC_ALGORITHM = "AES";
    private static final int BITS_KEY_SIZE = 256;
    private static final String PKCS12_FILE_EXT = ".p12";
    private static final String KEYSTORE_TYPE = "PKCS12";
    private static final String REMOVE_CMD = "-r";
    private static final String DISPLAY_CMD = "-d";
    private static final String GET_CMD = "-g";
    private static final String CREATE_KEYSTORE_CMD = "-c";

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage options: \n");

            System.out.println("* Create and store new key:");
            System.out.println("java staticversions.KeyHandler <keyStorePath> <keyAlias>");

            System.out.println("* Retrieve specified key");
            System.out.println("java staticversions.KeyHandler <keyStorePath> -g <keyAlias>");

            System.out.println("* Remove specified key:");
            System.out.println("java staticversions.KeyHandler <keyStorePath> -r <keyAlias>");

            System.out.println("* Display keys in keystore:");
            System.out.println("java staticversions.KeyHandler <keyStorePath> -d");

            System.out.println("* Create new keystore at path");
            System.out.println("java staticversions.KeyHandler <keyStorePath> -c");

            System.out.println("\n[<keyStorePath>: Absolute path to the keystore, e.g. /Path/To/Keystore/keystore.p12]");
            return;
        }

        String keyStorePath = args[0];
        String operation = args.length > 1 ? args[1] : "create";
        char[] password;

        try {
            switch (operation.toLowerCase()) {
                case CREATE_KEYSTORE_CMD -> {
                    if (args.length != 2) {
                        System.out.println("Incorrect number of arguments for 'create keystore' operation.");
                        return;
                    }
                    password = createKeystorePassword();
                    createKeyStore(keyStorePath, password);
                }
                case GET_CMD -> {
                    if (args.length != 3) {
                        System.out.println("Incorrect number of arguments for 'get key' operation.");
                        return;
                    }
                    password = promptForPassword();
                    SecretKey key = getKeyFromKeyStore(args[2], keyStorePath, password);
                    System.out.println(Base64.getEncoder().encodeToString(key.getEncoded()));
                }
                case REMOVE_CMD -> {
                    if (args.length != 3) {
                        System.out.println("Incorrect number of arguments for 'remove' operation.");
                        return;
                    }
                    password = promptForPassword();
                    removeKeyFromKeyStore(args[2], keyStorePath, password);
                }
                case DISPLAY_CMD -> {
                    if (args.length != 2) {
                        System.out.println("Incorrect number of arguments for 'display' operation.");
                        return;
                    }
                    password = promptForPassword();
                    displayKeysInKeyStore(keyStorePath, password);
                }
                default -> {
                    if (args.length != 2) {
                        System.out.println("Incorrect number of arguments for 'create key' operation.");
                        return;
                    }
                    password = promptForPassword();
                    SecretKey secretKey = generateSecretKey();
                    saveKeyToKeyStore(secretKey, keyStorePath, args[1], password);
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Error: The specified file does not exist.");
        } catch (SecurityException ex) {
            System.out.println("Error: Insufficient permissions to access or create the KeyStore.");
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("An error occurred: " + ex.getMessage());
        }
    }

    // CREATE PASSWORD
    private static char[] createKeystorePassword() {
        Console console = System.console();
        if (console == null) {
            System.out.println("Couldn't get Console instance");
            System.exit(0);
        }

        while (true) {
            char[] password = console.readPassword("Enter password: ");
            char[] confirmPassword = console.readPassword("Confirm password: ");
            if (Arrays.equals(password, confirmPassword)) {
                Arrays.fill(confirmPassword, '0');
                return password;
            }
            System.out.println("Passwords don't match! Try again.");
        }
    }

    // PASSWORD CHECK
    private static char[] promptForPassword() {
        Console console = System.console();
        if (console == null) {
            System.out.println("Couldn't get Console instance");
            System.exit(0);
        }
        return console.readPassword("Enter keystore password: ");
    }

    // STORE KEYSTORE BACK TO FILE
    private static void storeKeyStoreToFile(KeyStore keyStore, String keyStorePath, char[] password) throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException {
        try (OutputStream fos = new FileOutputStream(keyStorePath)) {
            keyStore.store(fos, password);
        }
    }


    // CREATE KEY
    private static SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(CRYPTOGRAPHIC_SYMMETRIC_ALGORITHM);
        keyGen.init(BITS_KEY_SIZE);
        return keyGen.generateKey();
    }

    private static void saveKeyToKeyStore(SecretKey secretKey, String keyStorePath, String keyAlias, char[] password)
            throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
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

    // GET
    private static SecretKey getKeyFromKeyStore(String keyAlias, String keyStorePath, char[] password)
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, IOException {
        KeyStore keyStore = loadExistingKeyStore(keyStorePath, password);

        if (!keyStore.containsAlias(keyAlias)) {
            throw new KeyStoreException("Key alias not found.");
        }

        return (SecretKey) keyStore.getKey(keyAlias, password);
    }

    // REMOVE
    private static void removeKeyFromKeyStore(String keyAlias, String keyStorePath, char[] password)
            throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        KeyStore keyStore = loadExistingKeyStore(keyStorePath, password);
        if (!keyStore.containsAlias(keyAlias)) {
            System.out.println("Key alias not found.");
            return;
        }

        keyStore.deleteEntry(keyAlias);

        storeKeyStoreToFile(keyStore, keyStorePath, password);

        System.out.println("Key with alias " + keyAlias + " removed successfully.");
    }

    // DISPLAY
    private static void displayKeysInKeyStore(String keyStorePath, char[] password)
            throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
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

    // CREATE KEYSTORE
    private static void createKeyStore(String keyStorePath, char[] password)
            throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {

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

    // LOAD KEYSTORE
    private static KeyStore loadExistingKeyStore(String keyStorePath, char[] password)
            throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {

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



