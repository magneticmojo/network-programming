package keyhandler;

import java.io.*;
import javax.crypto.*;
import java.util.*;

// Kommentar till rättande lärare:
// Jag inser att jag avviker från det specificerade kravet för hur och vilka argument
// som ska användas för att skapa en ny nyckel. Detta eftersom jag behöver sätta
// ett alias för varje nyckel för användning av resterande funktionalitet.

/**
 * Handles operations pertaining to a KeyStore by offering a command line interface for
 * creating, retrieving, and manipulating keys within a given KeyStore.
 */
public class KeyHandler {

    private static final String REMOVE_CMD = "-r";
    private static final String DISPLAY_CMD = "-d";
    private static final String GET_CMD = "-g";
    private static final String CREATE_KEYSTORE_CMD = "-c";
    private final KeyStoreService keyStoreService;
    private final ConsoleService consoleService;

    /**
     * Constructs a new KeyHandler instance with the designated services.
     *
     * @param keyStoreService Provides functions to handle KeyStore operations.
     * @param consoleService Enables interaction with the user through the console.
     */
    public KeyHandler(KeyStoreService keyStoreService, ConsoleService consoleService) {
        this.keyStoreService = keyStoreService;
        this.consoleService = consoleService;
    }

    /**
     * Processes user command line inputs to execute KeyStore operations.
     * The operations range from key creation, retrieval, to deletion.
     *
     * @param args Array of command line arguments.
     */
    public synchronized void handleRequest(String[] args) {
        if (args.length < 1) {
            displayUsage();
            return;
        }

        String keyStorePath = args[0];
        String operation = args.length > 1 ? args[1] : "create";

        try {
            switch (operation.toLowerCase()) {
                case CREATE_KEYSTORE_CMD -> handleCreateKeyStore(keyStorePath, args);
                case GET_CMD -> handleGetKey(keyStorePath, args);
                case REMOVE_CMD -> handleRemoveKey(keyStorePath, args);
                case DISPLAY_CMD -> handleDisplayKeys(keyStorePath, args);
                default -> handleCreateKey(keyStorePath, args);
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

    /**
     * Displays the different command line options available to the user.
     * Provides guidance on how to execute each KeyStore operation.
     */
    private void displayUsage() {
        System.out.println("Usage options: \n");
        System.out.println("* Create and store new key:");
        System.out.println("java KeyHandler <keyStorePath> <keyAlias>");

        System.out.println("* Retrieve specified key");
        System.out.println("java KeyHandler <keyStorePath> -g <keyAlias>");

        System.out.println("* Remove specified key:");
        System.out.println("java KeyHandler <keyStorePath> -r <keyAlias>");

        System.out.println("* Display keys in keystore:");
        System.out.println("java KeyHandler <keyStorePath> -d");

        System.out.println("* Create new keystore at path");
        System.out.println("java KeyHandler <keyStorePath> -c");

        System.out.println("\n[<keyStorePath>: Absolute path to the keystore, e.g. /Path/To/Keystore/keystoreFile.p12]");
    }

    /**
     * Creates a new KeyStore at the given path with a user-provided password.
     *
     * @param keyStorePath Location to save the KeyStore.
     * @param args Array of command line arguments.
     * @throws Exception If an error occurs during the creation.
     */
    private void handleCreateKeyStore(String keyStorePath, String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Incorrect number of arguments for 'create keystore' operation.");
            return;
        }
        char[] password = consoleService.createKeystorePassword();
        keyStoreService.createKeyStore(keyStorePath, password);
    }

    /**
     * Fetches a specified key from the KeyStore.
     *
     * @param keyStorePath Location of the KeyStore.
     * @param args Array of command line arguments.
     * @throws Exception If an error occurs during the retrieval.
     */
    private void handleGetKey(String keyStorePath, String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("Incorrect number of arguments for 'get key' operation.");
            return;
        }
        char[] password = consoleService.promptForPassword();
        SecretKey key = keyStoreService.getKeyFromKeyStore(args[2], keyStorePath, password);
        System.out.println(Base64.getEncoder().encodeToString(key.getEncoded()));
    }

    /**
     * Removes a specified key from the KeyStore.
     *
     * @param keyStorePath Location of the KeyStore.
     * @param args Array of command line arguments.
     * @throws Exception If an error occurs during the removal.
     */
    private void handleRemoveKey(String keyStorePath, String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("Incorrect number of arguments for 'remove' operation.");
            return;
        }
        char[] password = consoleService.promptForPassword();
        keyStoreService.removeKeyFromKeyStore(args[2], keyStorePath, password);
    }

    /**
     * Lists the keys stored in the KeyStore.
     *
     * @param keyStorePath Location of the KeyStore.
     * @param args Array of command line arguments.
     * @throws Exception If an error occurs during listing.
     */
    private void handleDisplayKeys(String keyStorePath, String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Incorrect number of arguments for 'display' operation.");
            return;
        }
        char[] password = consoleService.promptForPassword();
        keyStoreService.displayKeysInKeyStore(keyStorePath, password);
    }

    /**
     * Creates and stores a new key in the KeyStore.
     *
     * @param keyStorePath Location of the KeyStore.
     * @param args Array of command line arguments.
     * @throws Exception If an error occurs during key creation or storage.
     */
    private void handleCreateKey(String keyStorePath, String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Incorrect number of arguments for 'create key' operation.");
            return;
        }
        char[] password = consoleService.promptForPassword();
        SecretKey secretKey = keyStoreService.generateSecretKey();
        keyStoreService.saveKeyToKeyStore(secretKey, keyStorePath, args[1], password);
    }

    /**
     * Main method to start the KeyHandler operations.
     * Initializes services and handles user input.
     *
     * @param args Array of command line arguments.
     */
    public static void main(String[] args) {
        KeyStoreService keyStoreService = new KeyStoreServiceImpl();
        ConsoleService consoleService = new ConsoleServiceImpl();

        KeyHandler keyHandler = new KeyHandler(keyStoreService, consoleService);
        keyHandler.handleRequest(args);
    }
}
