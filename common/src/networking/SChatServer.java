package networking;

import crypto.Cryptography;

import javax.crypto.SecretKey;
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

    private final static Logger LOGGER = Logger.getLogger(SChatServer.class.getName());
    // the online list
    private HashMap<String, ObjectOutputStream> clients;
    private SecretKey skey;
    private KeyPair keyPair;

    /**
     * Initialize a new server, which is listening to the given port number
     *
     * @param portNumber the port number
     */
    public SChatServer(int portNumber) {
        LOGGER.setLevel(Level.INFO);
        LOGGER.info("Server has started...");
        clients = new HashMap<>();

        byte[] seed = new byte[2];
        seed[0] = 100;
        seed[1] = 101;
        // Should actually be loaded and not be generated
        skey = Cryptography.gen_symm_key(seed);
        keyPair = Cryptography.gen_asymm_key(seed);

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
     * @return the connected ObjectOutputStream of the given user
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
     * Return the symmetric key
     *
     * @return the symmetric key
     */
    public SecretKey getSkey() {
        return skey;
    }

    /**
     * Return the asymmetric key
     *
     * @return the asymmetric key
     */
    public KeyPair getKeyPair() {
        return keyPair;
    }

    /**
     * Usage:
     * java networking.SChatServer <port number>
     * @param args
     */
    public static void main(String[] args) {
        try {
            new SChatServer(Integer.parseInt(args[0]));
        } catch (Exception e) {
            System.err.println("usage: java networking.SChatServer <port>");
        }
    }
}
