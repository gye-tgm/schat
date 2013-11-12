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
    private String timestamp;

    /**
     * Creates a new message from the given parameters
     *
     * @param sender   the sender of this message
     * @param receiver the receiver of this message
     * @param message  the actual message
     */
    public ChatMessage(User sender, User receiver, String message) {
        //this(sender, receiver, message);
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        setActualTime();
    }

    /**
     * Initializes the timestamp with the current date and time.
     */
    public void setActualTime() {
        Calendar c = Calendar.getInstance();
        String min = "" + c.get(Calendar.MINUTE);
        if (Integer.parseInt(min) < 10) {
            min = "0" + min;
        }
        timestamp = "" + c.get(Calendar.HOUR_OF_DAY) + ":" + min + " | " + c.get(Calendar.DATE) + "." + c.get(Calendar.MONTH);
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
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String toString() {
        StringBuilder result = new StringBuilder("");
        //SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

        result.append("Timestamp: ");
        //result.append(getTimestamp() == null ? "Unknown" : sdf.format(getTimestamp().getTime()));
        result.append(getTimestamp()); //Timestamp can't be null

        result.append('\n');

        result.append("From: " + (sender.getName() == null ? "Unknown" : sender.getName()) + "\n");
        result.append("To: " + (receiver.getName() == null ? "Unknown" : receiver.getName()) + "\n");
        result.append("Message: " + message + "\n");
        return result.toString();
    }
}
