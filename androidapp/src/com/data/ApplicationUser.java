package com.data;

import data.DatabaseManager;
import data.SQLiteManager;
import data.User;
import data.contents.ChatContent;
import networking.SChatClient;
import networking.SChatServer;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Gary Ye
 * @version 12/2/13
 * One has to specify the following data:
 *  - host name (default: server name)
 *  - port ( default: port number )
 *
 */
public class ApplicationUser extends User {
    private static ApplicationUser instance = null;
    private SChatClient client;
    private DatabaseManager sqLiteManager;

    private final static String databaseName = "schat.db";
    private final static String hostName = "85.10.240.108";
    private final static int portNumber = SChatServer.PORT_ADDRESS;

    private ApplicationUser() throws IOException {
        this(databaseName, hostName, portNumber);
    }

    private ApplicationUser(String databaseName, String hostName, int portNumber) throws IOException {
        sqLiteManager = new SQLiteManager(databaseName);
        // pass this object as a reference so the chat listener is able to call the method 'receiveMessage'
        client = new SChatClient(this, hostName, portNumber);
    }

    public static ApplicationUser getInstance() throws IOException {
        if(instance == null){
            synchronized (ApplicationUser.class){
                if (instance == null) {
                    instance = new ApplicationUser();
                }
            }
        }
        return instance;
    }

    public boolean registerToServer(){
        try {
            client.registerToServer();
        } catch (IOException e) {
            e.printStackTrace();
        } // gui output...
        return true;
    }

    public boolean loginToServer(){
        try{
            client.loginToServer();
        } catch(IOException e){
            e.printStackTrace();
        } // gui output...
        return true;
    }

    public void sendMessage(String text, String receiverId){
        client.sendMessage(new ChatContent(text), receiverId);
        // show to GUI
    }

    public void receiveMessage(ChatContent chatContent){
        // show to gui...
    }

    public void addContact(User newContact){
        sqLiteManager.insertUser(newContact);
    }

    public void removeContact(String id){
        sqLiteManager.removeUser(id);
    }

    public ArrayList<User> loadContacts(){
        return sqLiteManager.loadUsers();
    }

    // Register to Server
    // Login to Server
    // Send message
    // Receive message
    // Add User to contact list
    // Load all user from contact list
    // Load all messages from a given user
}
