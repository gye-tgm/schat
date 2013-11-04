package com.networking;

import com.data.ChatMessage;
import com.data.User;

import java.io.IOException;
import java.net.Socket;

/**
 * This class manages the network communication between the client and the server
 * from the client's point of view.
 *
 * @author Gary Ye
 */
public class SChatClient extends Thread {
    public static final String SERVER_NAME = "85.10.240.108";
    public static final int PORT_ADDRESS = 1234;

    private User client;
    private Socket socket;
    private SChatClientListener listener;
    private SChatClientWriter sender;

    public SChatClient(User client) throws IOException {
        this(SERVER_NAME, PORT_ADDRESS, client);
    }

    public SChatClient(String hostName, int portNumber, User client) throws IOException {
        this.client = client;
        this.socket = new Socket(hostName, portNumber);
        this.listener = new SChatClientListener(socket, client);
        this.sender = new SChatClientWriter(socket);

        listener.start();
        sender.introduceToServer(client);
    }


    public void closeConnection() throws IOException {
        socket.close();
    }

    public void sendMessage(ChatMessage message) {
        sender.send(message);
    }

}
