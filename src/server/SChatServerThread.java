package server;

import com.data.ChatMessage;
import com.data.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author Gary
 * @version 10/23/13
 *          Handles one single client by using a thread.
 */
public class SChatServerThread extends Thread {
    private final SChatServer server;
    private Socket clientSocket;
    private User client;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public SChatServerThread(Socket clientSocket, SChatServer server) throws IOException {
        this.clientSocket = clientSocket;
        this.server = server;
        this.in = new ObjectInputStream(clientSocket.getInputStream());
        this.out = new ObjectOutputStream(clientSocket.getOutputStream());
        this.client = getClientInformation();
        server.addUser(client.getId(), out);
        System.out.println(client.getId() + " " + client.getName());
    }

    public User getClientInformation() {
        User user = null;
        try {
            int id = in.readInt();
            String name = in.readUTF();
            user = new User(id, name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void run() {
        try {
            ChatMessage message = null;
            while ((message = (ChatMessage) in.readObject()) != null) {
                sendMessage(message);
            }
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(ChatMessage message) throws IOException {
        ObjectOutputStream recOut = server.getObjectOutputStreamById(message.getReceiver().getId());
        try {
            recOut.writeObject(message);
            recOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
