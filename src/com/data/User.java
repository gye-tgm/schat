package com.data;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.security.KeyPair;

public class User implements Serializable {
    private String id;
    private KeyPair keyPair;     // asymmetric key
    private SecretKey secretKey; // symmetric key

    /**
     * Create a user with all attributes set to null
     */
    public User(){
        this(null, null, null);
    }

    /**
     * Create user with the given id
     * @param id
     */
    public User(String id) {
        this(id, null, null);
    }

    /**
     * Create user with the given id, KeyPair and the SecretKey.
     * @param id the id of the user
     * @param keyPair the key pair (asymmetric key) of the user
     * @param secretKey the secret key (symmetric key) of the user
     */
    public User(String id, KeyPair keyPair, SecretKey secretKey) {
        this.id = id;
        this.keyPair = keyPair;
        this.secretKey = secretKey;
    }

    public void receiveMessage(ChatMessage message){
        System.out.println("New message: " + message.toString());
    }

    /**
     * Return the id of the user
     * @return the id of the user
     */
    public String getId() {
        return id;
    }

    /**
     * Return the key pair
     * @return the key pair
     */
    public KeyPair getKeyPair() {
        return keyPair;
    }

    /**
     * Return the secret key
     * @return the secret key
     */
    public SecretKey getSecretKey() {
        return secretKey;
    }

    /**
     * Return the name of the user
     * @return the name of the user
     */
    public String getName(){
        return this.id; // TODO: give the user a name
    }
}
