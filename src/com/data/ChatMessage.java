package com.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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
        setActualTime();
    }

    /**
     * Creates a new message from the given parameters
     *
     * @param sender   the sender of this message
     * @param receiver the receiver of this message
     * @param message  the actual message
     */
    public ChatMessage(User sender, User receiver, String message, Calendar timestamp) {
        this(sender, receiver, message);
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
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

    public String getTimestamp() {
        String min = "" + timestamp.get(Calendar.MINUTE);
        if (Integer.parseInt(min) < 10) {
            min = "0" + min;
        }
        return ("" + timestamp.get(Calendar.HOUR_OF_DAY) + ":" + min + " | " + timestamp.get(Calendar.DATE) + "." + timestamp.get(Calendar.MONTH));
    }

    public void setTimestamp(Calendar timestamp) {
        this.timestamp = timestamp;
    }

    public String toString() {
        StringBuilder result = new StringBuilder("");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

        result.append("Timestamp: ");
        result.append(getTimestamp() == null ? "Unknown" : sdf.format(timestamp.getTime()));

        result.append('\n');

        result.append("From: " + (sender.getName() == null ? "Unknown" : sender.getName()) + "\n");
        result.append("To: " + (receiver.getName() == null ? "Unknown" : receiver.getName()) + "\n");
        result.append("Message: " + message + "\n");
        return result.toString();
    }
}
