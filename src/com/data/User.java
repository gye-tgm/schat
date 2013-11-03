package com.data;

import java.io.Serializable;
import java.security.KeyPair;

public class User implements Serializable{
    private transient KeyPair keyPair;
    private String name;
    private int id;

    /**
     * Constructor with a specified name
     *
     * @param name the name of the user
     */
    public User(String name) {
        this.name = name;
    }

    /**
     * Constructor with a specified id
     *
     * @param id the id of the user
     */
    public User(int id) {
        this.id = id;
    }

    public User(int id, String name){
        this.id = id;
        this.name = name;
    }

    public static int MAX_MESSAGE_LENGTH = 256;
    public static int SENDING_SUCCESSFUL = 0;
    public static int SENDING_FAILED = 1;

    /**
     * Send a message to another user.
     * If the sending was successful 0 will be returned
     * otherwise a corresponding error code will be returned.
     *
     * @param receiver
     * @param message
     * @return 0 if the process was done successfully
     *         an error code otherwise
     * @throws IllegalArgumentException will be thrown iff the message is too long
     */
    public int sendMessage(User receiver, String message) throws IllegalArgumentException {
        if (message.length() > MAX_MESSAGE_LENGTH)
            throw new IllegalArgumentException();
        /* ~ todo ~ */
        return SENDING_SUCCESSFUL;
    }

    public void receiveMessage(ChatMessage message){
        System.out.println("New message: " + message.toString());
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the value of the name
     */
    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
