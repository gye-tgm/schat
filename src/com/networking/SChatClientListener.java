package com.networking;

import com.data.ChatMessage;
import com.data.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * It will be listening to the server and
 * notify the user if the server has a new message
 * for him.
 * @author Gary Ye
 */
public class SChatClientListener extends Thread {
    private Socket socket;
    private User receiver;

    public SChatClientListener(Socket socket, User receiver) {
        this.socket = socket;
        this.receiver = receiver;
    }

    public void run(){
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(socket.getInputStream());
            while(!interrupted()){
                ChatMessage message = (ChatMessage) in.readObject();
                notifyUser(message);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void notifyUser(ChatMessage message){
        receiver.receiveMessage(message);
    }
}
