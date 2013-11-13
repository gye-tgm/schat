package com.crypto;

import javax.crypto.SecretKey;
import java.io.*;
import java.util.Arrays;

/**
 * Objects of this class will be transmitted through the web. This class contains an encrypted message and their corresponding MAC.
 * @author Elias Frantar
 * @version 11.11.2013
 */
public class Envelope implements Serializable {

    private byte[] message; // the encrypted message
    private byte[] signature; // the MAC

    /**
     * Creates an new envelope for the given message using the given key to compute the MAC
     * @param message the message (encrypted)
     * @param mac_key the key for MAC computation
     */
    public Envelope(S_Message message, SecretKey mac_key) throws IOException {
        this.message = serialize(message);
        this.signature = sign(this.message, mac_key);
    }

    /**
     * Computes the MAC of a given message.
     * @param message the serialized message (encrypted)
     * @param mac_key the key for MAC computation
     * @return the MAC of the given message
     */
    public byte[] sign(byte[] message, SecretKey mac_key) {
        return Cryptography.mac(mac_key, message);
    }

    /**
     * Serializes a message and returns the received byte[].
     * @param message the message to serialize
     * @return the byte[] of this message
     */
    private byte[] serialize(S_Message message) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(message);
        oos.close();

        return baos.toByteArray();
    }

    /**
     * Deserializes a given serialized S_Message
     * @param message the serialized message
     * @return the S_Message as an Object
     */
    private S_Message deserialize(byte[] message) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(message);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object o = ois.readObject();
        return (S_Message)o;
    }

    /**
     * Checks if the message has been tampered or if some sending-error has occured.
     * @param mac_key the key to compute the mac
     * @return true if MAC was valid; false otherwise
     */
    public boolean verifyMAC(SecretKey mac_key) {
        boolean verified = false;

         if(Arrays.equals(signature, sign(this.message, mac_key)))
            verified = true;

        return verified;
    }

    /* we only want getters because there should never be invalid message/MAC pairs in this class */
    public S_Message getMessage() throws IOException, ClassNotFoundException {
        return deserialize(message);
    }
    public byte[] getSignature() {
        return signature;
    }
}
