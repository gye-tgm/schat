package networking;

import crypto.Envelope;
import data.*;
import data.contents.ChatContent;
import data.contents.Login;
import data.contents.Registration;

import java.io.IOException;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Calendar;

/**
 * This class manages the network communication between the client and the server
 * from the client's point of view.
 *
 * @author Gary Ye
 */
public class SChatClient extends Thread {
    private User client;
    private Socket socket;
    private SChatClientListener listener;
    private SChatClientWriter sender;

    /**
     * Generate a SChatClient, which can listen and send messages
     * simultaneously.
     *
     * @param client the client to handle
     * @throws IOException
     */
    public SChatClient(User client) throws IOException {
        this(SChatServer.SERVER_NAME, SChatServer.PORT_ADDRESS, client);
    }

    /**
     * Generate a SChatClient, which can listen and send messages
     * simultaneously. It is going to interact with the given server.
     *
     * @param hostName the host name of the server
     * @param portNumber the port number
     * @param client the client
     * @throws IOException
     */
    public SChatClient(String hostName, int portNumber, User client) throws IOException {
        this.client = client;
        this.socket = new Socket(hostName, portNumber);
        this.listener = new SChatClientListener(socket, client);
        this.sender = new SChatClientWriter(socket);
        listener.start();
    }

    /**
     * Send the given ChatContent to the server with the given
     * receiver id.
     * @param chatContent the chat content
     * @param receiverId the receiver id
     */
    public void sendMessage(ChatContent chatContent, String receiverId) {
        Message<ChatContent> message = new Message<>(Calendar.getInstance().getTime(), client.getId(), receiverId, chatContent);
        sender.send(encrypt(message));
    }

    /**
     * Try to login to the server
     * @throws IOException
     */
    public void loginToServer() throws IOException {
        Message<Login> loginMessage = new Message<Login>(Calendar.getInstance().getTime(), client.getId(), SChatServer.SERVER_ID,
                new Login(client.getId()));
        sender.send(encrypt(loginMessage));
    }
    public void registerToServer() throws IOException{
        Message<Registration> registrationMessage =
        new Message<Registration>(
                Calendar.getInstance().getTime(),
                client.getId(),
                SChatServer.SERVER_ID,
                new Registration(new Login(client.getId()), client.getKeyPair().getPublic())
        );
        sender.send(encrypt(registrationMessage));
    }
    /**
     * Envelope a message
     * @param message the message
     * @return an enveloped message
     */
    public Envelope encrypt(Message<? extends Content> message){
        String receiverId = message.getReceiver();
        SQLiteManager sqLiteManager = new SQLiteManager("client.db");
        PublicKey receiverPublicKey = sqLiteManager.getPublicKeyFromId(receiverId);
        return new Envelope(message, client.getSecretKey(), receiverPublicKey, client.getKeyPair().getPrivate());
    }
}
