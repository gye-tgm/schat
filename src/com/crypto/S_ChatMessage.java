package com.crypto;

import com.data.ChatMessage;
import com.data.User;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

/**
 *  This class represents a secure (encrypted) Chatmessage, which will be sent.
 *  @author Elias Frantar (0.3)
 *  @version 2.11.2013 (0.3)
 */
public class S_ChatMessage implements Serializable {

    private static final long serialVersionUID = 0L; // we need this for serializing

    /* plain */
    private Calendar timestamp;
    private User sender;
    private User receiver;

    private byte[] iv;

    /* encrypted */
    private byte[] encrypted_message; // the encrypted message

    /**
     * Creates a new secure_Chatmessage-instance from a plain Chatmessage which will be encrypted using the given keys.
     * @param message the Chatmessage which should be encrypted
     * @param symm_key the key for the symmetric encryption
     */
    public S_ChatMessage(ChatMessage message, SecretKey symm_key) {
        this.timestamp = message.getTimestamp();
        this.sender = message.getSender();
        this.receiver = message.getReceiver();

        IvParameterSpec ivspec = Cryptography.gen_symm_IV();
        iv = ivspec.getIV();

        encrypt(message, symm_key, ivspec);
    }

    /**
     * Encrypts a plain message using the given keys.
     * @param message the ChatMessage to encrypt
     * @param symm_key the key to use for symmetric encryption
     * @param iv the initialization vector of the symmetric encryption
     */
    private void encrypt(ChatMessage message, SecretKey symm_key, IvParameterSpec iv) {
        encrypted_message = Cryptography.symm_crypt(symm_key, iv, message.getMessage().getBytes(), Cipher.ENCRYPT_MODE);
    }

    /**
     * Decrypts this message and returns the plain message.
     * @param symm_key the key to use for symmetric encryption
     * @param MAC_key the key for the Message Authentication Code
     * @param tag the MAC of the received message
     * @return the plain message; or null if anything failed
     */
    public ChatMessage decrypt(SecretKey symm_key, SecretKey MAC_key, byte[] tag) {

        ChatMessage message = null;
        try {
            if(verifyMac(tag, MAC_key)) {
                message = new ChatMessage(sender, receiver,
                                          new String(Cryptography.symm_crypt(symm_key, new IvParameterSpec(iv), encrypted_message, Cipher.DECRYPT_MODE), "US-ASCII"),
                                          timestamp);
            }
        } catch (UnsupportedEncodingException e) {
        }

        return message;
    }

    /**
     * Computes the Mac of the given serialized S_ChatMessage.
     * @param MAC_key the key used to compute the MAC
     * @return the Mac of the given S_ChatMessage
     */
    public byte[] getTag(SecretKey MAC_key) {

        byte[] data = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            oos.close();

            data = Cryptography.mac(MAC_key, baos.toByteArray());
        }
        catch(Exception e) {
        }

        return data;
    }

    /**
     * Checks if the message has been tampered or if some error has occured.
     * @param tag the received MAC of this message
     * @param MAC_key the key to compute the mac
     * @return true if MAC was valid; false otherwise
     */
    public boolean verifyMac(byte[] tag, SecretKey MAC_key) {

        boolean verified = false;

        try {
            byte[] mac = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            oos.close();

            mac = Cryptography.mac(MAC_key, baos.toByteArray());

            if(Arrays.equals(tag, mac))
                verified = true;
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return verified;
    }

    /**
     * Returns a String of this secret message properly formatted
     * @return the formatted String
     */
    public String toString() {
        StringBuilder result = new StringBuilder("");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

        result.append("Timestamp: ");
        result.append(getTimestamp() == null ? "Unknown" : sdf.format(getTimestamp().getTime()) + "\n");

        result.append("Sender: " + sender.getName() + "\n");
        result.append("Receiver: " + receiver.getName() + "\n");
        result.append("Initialization Vector: " + toHex(iv) + "\n");
        result.append("Encrypted Message: " + toHex(encrypted_message) + "\n");

        return result.toString();
    }

    /**
     * Converts a byte[] to a hex-String
     * @param bytes the byte[]to convert
     * @return the corresponding hex-String
     */
    private String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }

    /* only getters because data in an object of this class should not be modified after creation */
    public Calendar getTimestamp() {
        return timestamp;
    }
    public User getSender() {
        return sender;
    }
    public User getReceiver() {
        return receiver;
    }
    public byte[] getIv() {
        return iv;
    }
    public byte[] getEncrypted_message() {
        return encrypted_message;
    }

}
