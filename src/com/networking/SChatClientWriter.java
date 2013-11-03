package com.networking;

import com.crypto.S_ChatMessage;
import com.data.ChatMessage;
import com.data.User;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class sends messages through the given socket.
 * @author Gary Ye
 */
public class SChatClientWriter {
    private Socket socket;
    ObjectOutputStream out;
    public SChatClientWriter(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
    }

    public void send(ChatMessage message){
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace(); // TODO: return an error code indicating that the sending has failed; or other solution
        }
    }

    public void introduceToServer(User client) throws IOException {
        out.writeInt(client.getId());
        out.flush();
        out.writeUTF(client.getName());
        out.flush();
    }
}
