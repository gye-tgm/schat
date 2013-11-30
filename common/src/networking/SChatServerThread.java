package networking;

import crypto.Envelope;
import data.Message;
import data.contents.Login;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.KeyPair;

/**
 * @author Gary
 * @version 2013/11/29
 *          This single server thread handles one single client.
 */
public class SChatServerThread extends Thread {
    private final SChatServer server;
    private Socket clientSocket;
    private String client;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    /**
     * Create a server thread, which will be handle a single client connected
     * to this server.
     *
     * @param clientSocket the socket of the client
     * @param server       the server
     * @throws IOException
     */
    public SChatServerThread(Socket clientSocket, SChatServer server) throws IOException {
        this.clientSocket = clientSocket;
        this.server = server;
        this.in = new ObjectInputStream(clientSocket.getInputStream());
        this.out = new ObjectOutputStream(clientSocket.getOutputStream());
        this.client = null;
    }

    /**
     * Handle the login of the user and check whether it was successful or not.
     *
     * @param login the login data
     * @return whether the login of the user was successful or not
     */
    public boolean handleLogin(Login login) {
        if (server.findUser(login.getId())) {
            client = login.getId();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Return whether the user is logged in or not.
     *
     * @return whether the user is logged in or not
     */
    public boolean isLoggedIn() {
        return client != null;
    }

    /**
     * Run the main logic of this server
     */
    public void run() {
        // Load the symmetric key and the key pair from the database
        SecretKey skey = server.getSkey();
        KeyPair keypair = server.getKeyPair();

        boolean isRunning = true;
        while (isRunning) {
            try {
                Envelope envelope = (Envelope) in.readObject();
                switch (envelope.getType()) {
                    case CHAT_MESSAGE:
                        if(!isLoggedIn()){
                            isRunning = false;
                        }else{
                            // Message<ChatContent> chatContentMessage = envelope.decryptMessage(skey, keypair.getPublic());
                            // chatContentMessage.getReceiver();
                            // chatContentMessage.getSender();
                            // envelope.decryptMessage()
                        }
                        break;
                    case LOGIN:
                        Message<Login> loginMessage = envelope.decryptMessage(skey, keypair.getPublic());
                        if (!handleLogin(loginMessage.getContent()))
                            isRunning = false;
                        break;
                }
            } catch (IOException e) {
                System.err.println("Connection to " + client + " closed.");
                break;
            } catch (ClassNotFoundException e) {
                System.err.println("Class not found: " + e.getMessage());
                break;
            }
        }
        // Close and kill everything
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.eraseUser(client);
    }
}
