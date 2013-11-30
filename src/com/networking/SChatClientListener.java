package com.networking;

import com.crypto.Envelope;
import com.data.ChatMessage;
import com.data.Message;
import com.data.User;
import com.data.contents.ChatContent;
import com.data.contents.Login;

import javax.crypto.SecretKey;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.KeyPair;

/**
 * It will be listening to the server and
 * notify the user if the server has a new message
 * for him.
 *
 * @author Gary Ye
 */
public class SChatClientListener extends Thread {
    private Socket socket;
    private User receiver;

    /**
     * Create a new listener thread, which is constantly listening
     * to the server.
     * @param socket the socket to listen to
     * @param receiver the receiver or the client to handle
     * @throws IOException
     */
    public SChatClientListener(Socket socket, User receiver) throws IOException{
        this.socket = socket;
        this.receiver = receiver;
    }

    public void run() {
        boolean isRunning = true;
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (isRunning) {
            try {
                Envelope envelope = (Envelope) in.readObject();
                switch (envelope.getType()) {
                    case CHAT_MESSAGE:
                        notifyUser(envelope);
                        break;
                    default:
                        System.err.println("Unknown envelope type!");
                        isRunning = false;
                        break;
                }
            } catch (IOException e) {
                System.err.println("Connection to the server closed.");
                break;
            } catch (ClassNotFoundException e) {
                System.err.println("Class not found: " + e.getMessage());
                break;
            }
        }

        try {
            in.close();
        } catch (IOException ignored) {}
    }

    /**
     * Notify the user with the received envelope
     * @param envelope the envelope
     */
    public void notifyUser(Envelope envelope) {
        receiver.receiveMessage(envelope);
    }
}
