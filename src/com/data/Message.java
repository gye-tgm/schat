package com.data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class represents a plain message(nothing is encrypted or authenticated). This message can contain any type of "Content".
 * @author Elias Frantar
 * @version 15.11.2013
 */
public class Message {

    /* protected because we want to have these attributes in the secure message class */
    protected Date timestamp;
    protected String sender;
    protected String receiver;

    private Content content; // this should >> not << be in any subclasses, so private

    /**
     * Creates a new message from the given information
     * @param timestamp the time the message was created
     * @param sender the sender of the message
     * @param receiver the receiver of the message
     * @param content the content of the message
     */
    public Message(Date timestamp, String sender, String receiver, Content content) {
        this.timestamp = timestamp;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }

    /**
     * Creates a new message without content. This constructor should only be called from the SecureMessage-class.
     * @param timestamp the time the message is created
     * @param sender the sender of the message
     * @param receiver the receiver of the message
     */
    protected Message(Date timestamp, String sender, String receiver) { // this constructor should only be accessed from the secure message class, so protected
        this.timestamp = timestamp;
        this.sender = sender;
        this.receiver = receiver;
    }

    /**
     * Returns this message as a properly formatted and readable String.
     * @return this message as a String
     */
    public String toString() {
        StringBuilder result = new StringBuilder("");

        result.append(requiredInfoToString());
        result.append(content); // automatically calls the toString()-method

        return result.toString();
    }

    /**
     * Returns the timestamp, sender and receiver of this message in a properly formatted and readable String.
     * @return the timestamp, sender and receiver as a String
     */
    protected String requiredInfoToString() { // should be accessible for sub-classes
        StringBuilder result = new StringBuilder("");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

        result.append(sdf.format(timestamp) + "\n");
        result.append("Sender: " + sender + "\n");
        result.append("Receiver: " + receiver + "\n");

        return result.toString();
    }

    /* once data is saved in an object of this class, it should not be modified anymore */
    public Date getTimestamp() {
        return timestamp;
    }
    public String getSender() {
        return sender;
    }
    public String getReceiver() {
        return receiver;
    }
    public Content getContent() {
        return content;
    }
}
