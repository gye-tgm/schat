package server;

import com.data.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * @author Gary Ye
 * @version 10/23/2013
 *      The KnockKnockServer allows multiple clients connecting
 *      to itself and will be telling them KnockKnock jokes.
 */
public class SChatServer {
    private HashMap<Integer, Socket> clients;

    /**
     * Initializes a new KnockKnock Server and also
     * starts the service.
     * @param portNumber the port number listening to
     */
    public SChatServer(int portNumber) {
        clients = new HashMap<>();

        boolean isListening = true;
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (isListening) {
                new SChatServerThread(serverSocket.accept(), this).start();
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.exit(1);
        }
    }

    public void addUser(int id, Socket socket){
        clients.put(id, socket);
    }
    public void eraseUser(int id){
        clients.remove(id);
    }

    public Socket getSocket(int id) {
        return clients.get(id);
    }
}
