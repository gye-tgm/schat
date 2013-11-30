package com.networking;

import com.crypto.Envelope;
import com.data.ChatMessage;
import com.data.Content;
import com.data.Message;
import com.data.User;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class sends messages through the given socket.
 *
 * @author Gary Ye
 * @version 2013/11/30
 */
public class SChatClientWriter {
    private Socket socket;
    ObjectOutputStream out;

    /**
     * Create a writer which is only writing to the ObjectOutputStream
     * @param socket the socket to write to
     * @throws IOException
     */
    public SChatClientWriter(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
    }

    /**
     * Send an envelope to the server
     * @param message
     */
    public void send(Envelope message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace(); // TODO: return an error code indicating that the sending has failed; or other solution
        }
    }

}
