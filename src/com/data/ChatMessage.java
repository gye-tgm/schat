package com.data;

import java.io.Serializable;
import java.util.Calendar;

/**
 * This class represents a chat message.
 *
 * @author Elias Frantar (0.1)
 * @version 14.10.2013: 0.1
 */
public class ChatMessage implements Serializable {
    private User sender;
    private User receiver;

    private String message;

    private Calendar timestamp;

    /**
     * Creates a new message from the given parameters
     *
     * @param sender   the sender of this message
     * @param receiver the receiver of this message
     * @param message  the actual message
     */
    public ChatMessage(User sender, User receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    /**
     * Creates a new message from the given parameters
     *
     * @param sender    the sender of this message
     * @param receiver  the receiver of this message
     * @param message   the actual message
     * @param timestamp the date and time the message was sent
     */
    public ChatMessage(User sender, User receiver, String message, Calendar timestamp) {
        this(sender, receiver, message);
        this.timestamp = timestamp;
    }


    /**
     * Initializes the timestamp with the current date and time.
     */
    public void setActualTime() {
        timestamp = Calendar.getInstance();
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Calendar timestamp) {
        this.timestamp = timestamp;
    }
}
