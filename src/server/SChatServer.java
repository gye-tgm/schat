package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * The SChat Server
 *
 * @author Gary Ye
 */
public class SChatServer {
    private HashMap<Integer, Socket> clients;

    /**
     * Initializes a new KnockKnock Server and also
     * starts the service.
     *
     * @param portNumber the port number listening to
     */
    public SChatServer(int portNumber) {
        clients = new HashMap<>();

        boolean isListening = true;
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            while (isListening) {
                new SChatServerThread(serverSocket.accept(), this).start();
            }
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.exit(1);
        }
    }

    public void addUser(int id, Socket socket) {
        clients.put(id, socket);
    }

    public void eraseUser(int id) {
        clients.remove(id);
    }

    public Socket getSocket(int id) {
        return clients.get(id);
    }
}
