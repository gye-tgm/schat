package networking;

import crypto.Envelope;
import data.Message;
import data.SQLiteManager;
import data.User;
import data.contents.Login;
import data.contents.PublicKeyRequest;
import data.contents.PublicKeyResponse;
import data.contents.Registration;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * @author Gary
 * @version 2013/11/29
 *          This single server thread handles one single client.
 */
public class SChatServerThread extends Thread {
    private final SChatServer server;
    private Socket clientSocket;
    private User client;
    private SecretKey secretKey;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private SQLiteManager dbmanager;

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
        this.dbmanager = new SQLiteManager(SChatServer.SERVER_DB);
    }

    /**
     * Handle the login of the user and check whether it was successful or not.
     *
     * @param login the login data
     * @return whether the login of the user was successful or not
     */
    public boolean handleLogin(Login login) {
        if (server.findUser(login.getId())) {
            client = dbmanager.getUserFromGivenId(login.getId());
            server.addUser(client.getId(), out);
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


    public void sendStoredMessages() {
        ArrayList<Envelope> envelopes = dbmanager.loadEncryptedMessagesFromReceiver(client.getId());
        int i = 0;
        for (; i < envelopes.size(); i++)
            if (!sendEnvelope(envelopes.get(i)))
                break;
        while (i-- > 0) {
            // remove from database
        }
    }

    /**
     * Run the main logic of this server
     */
    public void run() {
        boolean isRunning = true;

        // sendStoredMessages();

        while (isRunning) {
            try {
                Envelope envelope = (Envelope) in.readObject();
                switch (envelope.getType()) {
                    case CHAT_MESSAGE:
                        if (!isLoggedIn()) {
                            isRunning = false;
                        } else {
                            handleRedirecting(envelope);
                        }
                        break;
                    case LOGIN:
                        // envelope.de
                        Message<Login> loginMessage = envelope.<Login>decryptMessage(secretKey, server.getKeyPair().getPublic());
                        if (!handleLogin(loginMessage.getContent()))
                            isRunning = false;
                        break;
                    case REGISTRATION:
                        if (!handleRegistration(envelope)) {
                            isRunning = false;
                        }
                        break;
                    case PUBLIC_KEY_REQUEST:
                        handlePublicKeyRequest(envelope);
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
        server.eraseUser(client.getId());
    }

    private void handlePublicKeyRequest(Envelope envelope) {
        PublicKeyRequest pkRequest = envelope.<PublicKeyRequest>decryptMessage(secretKey, client.getPublicKey()).getContent();
        PublicKey requestPK = dbmanager.getPublicKeyFromId(pkRequest.getRequestId());
        Message<PublicKeyResponse> pkResponse = new Message<PublicKeyResponse>(Calendar.getInstance().getTime(), SChatServer.SERVER_ID,
                envelope.getSender(), new PublicKeyResponse(pkRequest.getRequestId(), requestPK));
        PublicKey senderPk = dbmanager.getPublicKeyFromId(envelope.getSender());
        sendEnvelope(new Envelope(pkResponse, secretKey, senderPk, server.getKeyPair().getPrivate()));
    }

    private void handleRedirecting(Envelope envelope) {
        redirectMessage(envelope);
    }

    public void redirectMessage(Envelope envelope) {
        if (!sendEnvelope(envelope)) {
            dbmanager.insertEncryptedMessage(envelope);
        }
    }

    /**
     * @param envelope
     * @return whether the sending was successful or not
     */
    public boolean sendEnvelope(Envelope envelope) {
        ObjectOutputStream out = server.getObjectOutputStreamById(envelope.getReceiver());
        if (out == null)
            return false;
        try {
            out.writeObject(envelope);
            out.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Handle the registration of a user.
     * The registration can fail if the given id already exists.
     *
     * @param envelope the envelope
     * @return if the registration was successful or not
     */
    private boolean handleRegistration(Envelope envelope) {
        this.secretKey = envelope.getUnwrappedKey(server.getKeyPair().getPrivate());
        Registration registration = envelope.<Registration>decryptMessage(secretKey).getContent();
        Login login = registration.getLogin();

        if (dbmanager.userExists(login.getId())) {
            System.err.println("Registration failed because id already exists");
            return false;
        }
        client = new User(login.getId(), new KeyPair(registration.getPublicKey(), null), secretKey);
        dbmanager.insertUser(client);
        System.out.println("New user " + login.getId() + " registered!");
        handleLogin(registration.getLogin());
        return true;
    }
}
