package data;

import java.io.Serializable;

/**
 * This is the super-class of all message contents. The message content, is the part of the message, which will be sealed before
 * transmission.
 * @author Elias Frantar
 * @version 15.11.2013
 */
public abstract class Content implements Serializable { // there should not exist any instances of this class

    /**
     * The different content-types of a message.
     */
    public static enum Type {
        CHAT_MESSAGE,
        LOGIN,
        REGISTRATION;
    }

    protected transient Type type; // this attribute must not be serialized (it is also contained in the SecureMessage class

    /**
     * Returns the type of this content.
     * @return the content type (contained in the Type-enum)
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns the content, properly formatted and readable in a String.
     * @return the content in a String
     */
    public abstract String toString(); // must be overridden by every sub-class
}
