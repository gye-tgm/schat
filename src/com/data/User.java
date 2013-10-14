package com.data;

import java.security.KeyPair;

public class User {
    private KeyPair keyPair;
    private String name;

    /**
     * Constructor with a specified name
     *
     * @param name the name of the user
     */
    public User(String name) {
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
}
