package com.crypto;

import com.data.ChatMessage;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;

/**
 *  This class represents an encrypted message.
 *  @author Elias Frantar
 *  @version 11.11.2013
 */
public class S_ChatMessage extends S_Message {

    private byte[] iv;

    /* encrypted */
    private byte[] encrypted_message; // the encrypted message

    /**
     * Creates a new secure_Chatmessage-instance from a plain message which will be encrypted using the given keys.
     * @param message the Chatmessage which should be encrypted
     * @param symm_key the key for the symmetric encryption
     * @param message the plain message
     */
    public S_ChatMessage(ChatMessage message, SecretKey symm_key) {

        message_type = Type.CHATMESSAGE; // this is a Chat-message and will always stay one

        setSender(message.getSender());
        setReceiver(message.getReceiver());
        setTimestamp(message.getTimestamp());

        IvParameterSpec ivspec = Cryptography.gen_symm_IV();
        iv = ivspec.getIV();

        encrypted_message = encrypt(message.getMessage(), symm_key, ivspec);
    }

    /**
     * Encrypts a plain message using the given keys.
     * @param message the message to encrypt
     * @param symm_key the key to use for symmetric encryption
     * @param iv the initialization vector of the symmetric encryption
     * @return the encrypted message as a byte[]
     */
    private byte[] encrypt(String message, SecretKey symm_key, IvParameterSpec iv) {
        return Cryptography.symm_crypt(symm_key, iv, message.getBytes(), Cipher.ENCRYPT_MODE);
    }

    /**
     * Decrypts this message and returns the plain message.
     * @param symm_key the key to use for symmetric encryption
     * @return the plain message; or null if anything failed
     */
    public String decrypt(SecretKey symm_key) {
        String message = null;

        try {
             message = new String(Cryptography.symm_crypt(symm_key, new IvParameterSpec(iv), encrypted_message, Cipher.DECRYPT_MODE), "US-ASCII");
        } catch (UnsupportedEncodingException e) {
        }

        return message;
    }

    public String toString() {
        StringBuilder result = new StringBuilder("");

        result.append(plainToString());

        result.append("Initialization Vector: " + S_Message.toHex(iv) + "\n");
        result.append("Encrypted Message: " + S_Message.toHex(encrypted_message) + "\n");

        return result.toString();
    }

    /* we only want setters because the content of an object of this class should not be modified anymore */
    public byte[] getIv() {
        return iv;
    }
    public byte[] getEncrypted_message() {
        return encrypted_message;
    }

}
