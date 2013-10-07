package com.example.SChat;

import java.security.KeyPair;

public class User {
    private KeyPair keyPair;
    private String name;

    public User() {

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
     * @return
     * @throws IllegalArgumentException will be thrown iff the message is too long
     */
    public int sendMessage(User receiver, String message) throws IllegalArgumentException {
        if (message.length() > MAX_MESSAGE_LENGTH)
            throw new IllegalArgumentException();

        /* ~ todo ~ */
        return SENDING_SUCCESSFUL;
    }
}
