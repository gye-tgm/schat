package networking;

import data.DatabaseManager;
import data.KeyPairManager;
import data.SQLiteManager;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class runs a chat server.
 *
 * @author Gary Ye
 * @version 2013/11/29
 */
public class SChatServer {
    public static final String SERVER_NAME = "85.10.240.108";
    public static final int PORT_ADDRESS = 1234;
    public static final String SERVER_ID = "_";
    private static final String PUBLIC_KEY_FILE = "public.key";
    private static final String PRIVATE_KEY_FILE = "private.key";


    private final static Logger LOGGER = Logger.getLogger(SChatServer.class.getName());
    public static final String SERVER_DB = "server.db";
    private static final String SERVER_DB_SCRIPT = "serverdb.sql";
    // the online list
    private HashMap<String, ObjectOutputStream> clients;
    private KeyPair keyPair;
    private DatabaseManager databaseManager;

    /**
     * Initialize a new server, which is listening to the given port number
     *
     * @param portNumber the port number
     */
    public SChatServer(int portNumber) {
        LOGGER.setLevel(Level.INFO);
        LOGGER.info("Server has started...");

        keyPair = KeyPairManager.readKeyPair(PUBLIC_KEY_FILE, PRIVATE_KEY_FILE);
        LOGGER.info("Key pair loaded!");

        databaseManager = new SQLiteManager(SERVER_DB);
        databaseManager.createTables(SERVER_DB_SCRIPT);
        LOGGER.info("Database created");

        clients = new HashMap<>();
        LOGGER.info("User online list initialized");

        boolean isListening = true;
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            while (isListening) {
                new SChatServerThread(serverSocket.accept(), this).start();
            }
            serverSocket.close();
        } catch (IOException e) {
            LOGGER.warning("Exception caught when trying to listen on port " + portNumber);
            System.exit(1);
        }
    }

    /**
     * Add a user with a given ObjectOutputStream
     *
     * @param id  the id of the user
     * @param out the ObjectOutputStream of the user
     */
    public void addUser(String id, ObjectOutputStream out) {
        clients.put(id, out);
        LOGGER.info(id + " has logged in.");
    }

    /**
     * Remove the user from the currently online list.
     *
     * @param id the id of the user to remove
     */
    public void eraseUser(String id) {
        clients.remove(id);
        LOGGER.info(id + " has logged out.");
    }

    /**
     * Get the ObjectOutputStream of the given user id
     *
     * @param id the user id
     * @return the connected ObjectOutputStream of the given user or null if the user is not connected
     *         to the server
     */
    public ObjectOutputStream getObjectOutputStreamById(String id) {
        return clients.get(id);
    }

    /**
     * Check if the user exists in the database of the user list.
     *
     * @return
     */
    public boolean findUser(String id) {
        return true;
    }


    /**
     * Return the asymmetric key
     *
     * @return the asymmetric key
     */
    public KeyPair getKeyPair() {
        return keyPair;
    }

}
