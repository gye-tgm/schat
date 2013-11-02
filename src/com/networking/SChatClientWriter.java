package com.networking;

import com.crypto.S_ChatMessage;
import com.data.ChatMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class sends messages through the given socket.
 * @author Gary Ye
 */
public class SChatClientWriter {
    private Socket socket;
    public SChatClientWriter(Socket socket) {
        this.socket = socket;
    }

    public void send(ChatMessage message){
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())){
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace(); // TODO: return an error code indicating that the sending has failed; or other solution
        }
    }
}
