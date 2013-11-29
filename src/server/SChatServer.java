package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
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
    private final static Logger LOGGER = Logger.getLogger(SChatServer.class.getName());
    // the online list
    private HashMap<String, ObjectOutputStream> clients;

    /**
     * Initialize a new server, which is listening to the given port number
     * @param portNumber the port number
     */
    public SChatServer(int portNumber) {
        LOGGER.setLevel(Level.INFO);

        clients = new HashMap<>();

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
     * @param id the id of the user
     * @param out the ObjectOutputStream of the user
     */
    public void addUser(String id, ObjectOutputStream out) {
        clients.put(id, out);
        LOGGER.info(id + " has logged in.");
    }

    /**
     * Remove the user from the currently online list.
     * @param id the id of the user to remove
     */
    public void eraseUser(String id) {
        clients.remove(id);
        LOGGER.info(id + " has logged out.");
    }

    /**
     * Get the ObjectOutputStream of the given user id
     * @param id the user id
     * @return the connected ObjectOutputStream of the given user
     */
    public ObjectOutputStream getObjectOutputStreamById(String id) {
        return clients.get(id);
    }
}
