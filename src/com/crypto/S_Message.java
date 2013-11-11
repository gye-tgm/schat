package com.crypto;

import com.data.User;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This is the super-class of all secure contents that can be send in a message. It contains a definition of all different secure message types.
 * @author Elias Frantar
 * @version 11.11.2013
 */
public abstract class S_Message implements Serializable { // there should not exist any objects of this class

    protected Type message_type; // this attribute must be set in every Content class

    /* plain data required in every message */
    protected String sender;
    protected String receiver;
    protected Date timestamp; // Date is more compact to send than Calendar

    /**
     * The different types of message. Received messages will be processed differently depending on the set type.
     */
    public enum Type {
        /* todo: extend with other message types */
        CHATMESSAGE // a normal Chat-message from user to user with previously shared symmetric keys
    }

    /**
     * Returns the type of the message.
     * @return the type of the message (types are specified in the Type enum)
     */
    public Type getType() {
        return message_type;
    }

    /**
     * Returns a String of this secret message properly formatted
     * @return the formatted String
     */
    public abstract String toString();

    /**
     * Returns the plain data of a secure message as a String.
     * @return a String of the plain data
     */
    protected String plainToString() { // should be called by subclass toString()-methods
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        StringBuilder s = new StringBuilder();

        s.append(sdf.format(timestamp) + "\n");
        s.append("Sender: " + sender);
        s.append("Receiver: " + receiver);

        return s.toString();
    }


    /* setters() for the plain parts of a secure message from the original datatypes */
    public void setSender(User sender) {
        this.sender = sender.getName();
    }
    public void setReceiver(User receiver) {
        this.receiver = receiver.getName();
    }
    public void setTimestamp(Calendar timestamp) {
        this.timestamp = timestamp.getTime();
    }

    /* todo: add getters() with the correct return types */


    /**
     * Converts a byte[] to a hex-String
     * @param bytes the byte[]to convert
     * @return the corresponding hex-String
     */
    protected static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }
}