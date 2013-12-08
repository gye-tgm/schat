package data;

import crypto.Envelope;
import data.contents.ChatContent;
import data.contents.PublicKeyResponse;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class User implements Serializable {
    protected String id;
    protected KeyPair keyPair;     // asymmetric key
    protected SecretKey secretKey; // symmetric key

    /**
     * Create a user with all attributes set to null
     */
    public User() {
        this(null, null, null);
    }

    /**
     * Create user with the given id
     *
     * @param id
     */
    public User(String id) {
        this(id, null, null);
    }

    /**
     * Create user with the given id, KeyPair and the SecretKey.
     *
     * @param id        the id of the user
     * @param keyPair   the key pair (asymmetric key) of the user
     * @param secretKey the secret key (symmetric key) of the user
     */
    public User(String id, KeyPair keyPair, SecretKey secretKey) {
        this.id = id;
        this.keyPair = keyPair;
        this.secretKey = secretKey;
    }

    /**
     * Output the message to the console
     *
     * @param secure_message the secure message
     */
    public void receiveMessage(Envelope secure_message) {
        PublicKey senderPublicKey = (new SQLiteManager("client.db").getPublicKeyFromId(secure_message.getSender())); // Load from database
        SecretKey secretKey1 = secure_message.getUnwrappedKey(keyPair.getPrivate());
        ChatContent chatContent = secure_message.<ChatContent>decryptMessage(secretKey1, senderPublicKey).getContent();
        System.out.println(chatContent.toString());
    }

    /**
     * Returns the id of the user
     *
     * @return the id of the user
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the key pair
     *
     * @return the key pair
     */
    public KeyPair getKeyPair() {
        return keyPair;
    }

    /**
     * Returns the secret key
     *
     * @return the secret key
     */
    public SecretKey getSecretKey() {
        return secretKey;
    }

    /**
     * Returns the public key of the given user.
     *
     * @return the public key
     */
    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    /**
     * Returns the private key
     *
     * @return the private key
     */
    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }


    /**
     * Return the name of the user
     *
     * @return the name of the user
     */
    public String getName() {
        return this.id; // TODO: give the user a name
    }

    public void registerUser(Envelope envelope) {
        PublicKey senderPublicKey = (new SQLiteManager("client.db").getPublicKeyFromId(envelope.getSender())); // Load from database
        SecretKey secretKey1 = envelope.getUnwrappedKey(keyPair.getPrivate());
        PublicKeyResponse publicKeyResponse = envelope.<PublicKeyResponse>decryptMessage(secretKey1).getContent();
        SQLiteManager sqLiteManager = new SQLiteManager("client.db");
        sqLiteManager.insertUser(new User(publicKeyResponse.getRequestId(),
                new KeyPair(publicKeyResponse.getPublicKey(), null), null));
    }

}
