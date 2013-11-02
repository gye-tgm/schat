package server;

import com.data.ChatMessage;
import com.data.User;

import java.io.*;
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

    public SChatServerThread(Socket clientSocket, SChatServer server) {
        this.clientSocket = clientSocket;
        this.server = server;
        this.client = getClientInformation();
        server.addUser(client.getId(), clientSocket);
    }

    public User getClientInformation(){
        try(
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())){
            int id = in.readInt();
            String name = in.readUTF();
            return new User(id, name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())){
            ChatMessage message = null;
            while((message = (ChatMessage) in.readObject()) != null){
                sendMessage(message);
            }
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void sendMessage(ChatMessage message){
        Socket receiverSocket = server.getSocket(message.getReceiver().getId());
        try(ObjectOutputStream out = new ObjectOutputStream(receiverSocket.getOutputStream())){
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
