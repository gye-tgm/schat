package com.crypto;

import com.data.Content;
import com.data.Message;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;

/**
 * Instances of this class will be sent over the web. This class contains a signed and sealed message. It provides easier access to those attributes.
 * @author Elias Frantar
 * @version 29.11.2013
 */
public class Envelope {

    SignedObject sign_sec_message;

    /**
     * Generates a transmittable Envelope containing a secure signed and sealed Object.
     * @param message the message to seal and sign
     * @param key the symmetric key for sealing
     * @param signKey the private key for signing
     */
    public Envelope(Message message, SecretKey key, PrivateKey signKey) {
        SecureMessage secureMessage = new SecureMessage(message, key);

        try {
            sign_sec_message = new SignedObject(secureMessage, signKey, Cryptography.getSignature());
        }
        catch(InvalidKeyException e) {}
        catch(SignatureException e) {}
        catch(IOException e) {}
    }

    /**
     * Decrypts the message contained in this envelope and returns the plain message of the specified type.
     * @param key the symmetric key for content decryption
     * @param verificationKey the public key of the sender to verify the signature
     * @param <C> the requested return type
     * @return the plain message of the requested type; null if the signature did not verify
     */
    public <C extends Content> Message<C> decryptMessage(SecretKey key, PublicKey verificationKey) {
        Message<C> message = null;

        try {
            if(sign_sec_message.verify(verificationKey, Cryptography.getSignature())) {
                SecureMessage verifiedMessage = (SecureMessage)sign_sec_message.getObject();
                message = verifiedMessage.<C>decrypt(key);
            }
        }
        catch(InvalidKeyException e) {}
        catch(SignatureException e) {}
        catch(ClassNotFoundException e) {}
        catch(IOException e) {}
        catch(Exception e) {}

        return message;
    }

    /**
     * Returns the type of the received message.
     * @return the type of the message contained in this envelope
     */
    public Content.Type getType() throws ClassCastException {
        Content.Type type = null;

        try {
            SecureMessage secureMessage = (SecureMessage)sign_sec_message.getObject();
            type = secureMessage.getContentType();
        }
        catch (IOException e) {}
        catch (ClassNotFoundException e) {}
        catch (ClassCastException e) {}

        return type;
    }

    /**
     * Returns the sender of the received message.
     * @return the sender of the message contained in this envelope
     */
    public String getSender() {
        String sender = null;

        try {
            SecureMessage secureMessage = (SecureMessage)sign_sec_message.getObject();
            sender = secureMessage.getSender();
        }
        catch (IOException e) {}
        catch (ClassNotFoundException e) {}
        catch (ClassCastException e) {}

        return sender;
    }

    /**
     * Returns the receiver of the received message.
     * @return the receiver of the message contained in this envelope
     */
    public String getReceiver() {
        String receiver = null;

        try {
            SecureMessage secureMessage = (SecureMessage)sign_sec_message.getObject();
            receiver = secureMessage.getReceiver();
        }
        catch (IOException e) {}
        catch (ClassNotFoundException e) {}

        return receiver;
    }

    /**
     * Converts a byte[] to a hex-String
     * @param bytes the byte[] to convert
     * @return the corresponding hex-String
     */
    public static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }

}
