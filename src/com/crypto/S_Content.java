package com.crypto;

import com.data.User;

import java.io.Serializable;
import java.util.Calendar;

/**
 * This is the super-class of all secure contents that can be send in a message. It contains a definition of all different secure message types.
 * @auther Elias Frantar
 * @version 11.11.2013
 */
public abstract class S_Content implements Serializable { // there should not exist any objects of this class

    protected Type message_type; // this attribute must be set in every Content class

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

}
