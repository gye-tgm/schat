package com.crypto;

import com.data.Content;
import com.data.Message;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;

/**
 * This class provides an easy interface for message encryption and decryption, which automatically handles the different classes
 * required for en- and decryption. It also provides some other crypto-utilities.
 * @author Elias Frantar
 * @version 16.11.2013
 */
public class CryptoTools {

    /**
     * Generates a transmittable secure signed and sealed Object.
     * @param message the message to seal and sign
     * @param key the symmetric key for sealing
     * @param signKey the private key for signing
     * @return a secure transmittable SignedObject
     */
    public static SignedObject encryptMessage(Message message, SecretKey key, PrivateKey signKey) {
        SignedObject signedObject = null;

        SecureMessage secureMessage = new SecureMessage(message, key);

        try {
            signedObject = new SignedObject(secureMessage, signKey, Cryptography.getSignature());
        }
        catch(InvalidKeyException e) {}
        catch(SignatureException e) {}
        catch(IOException e) {}

        return signedObject;
    }

    /**
     * Decrypts a received message and returns the plain message of the specified type.
     * @param secureMessage the received SignedObject(must be a SecureMessage)
     * @param key the symmetric key for content decryption
     * @param verificationKey the public key of the sender to verify the signature
     * @param <C> the requested return type
     * @return the plain message of the requested type
     */
    public static <C extends Content> Message<C> decryptMessage(SignedObject secureMessage, SecretKey key, PublicKey verificationKey) {
        Message<C> message = null;

        try {
            if(secureMessage.verify(verificationKey, Cryptography.getSignature())) {
                SecureMessage verifiedMessage = (SecureMessage)secureMessage.getObject();
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
     * Returns the type of the received message. (Warning: the signed object should contain a SecureMessage)
     * @param message the SignedObject (containing a SecureMessage)
     * @return the type of the message contained in this SignedObject
     */
    public static Content.Type getType(SignedObject message) throws ClassCastException {
        Content.Type type = null;

        try {
            SecureMessage secureMessage = (SecureMessage)message.getObject();
            type = secureMessage.getContentType();
        }
        catch (IOException e) {}
        catch (ClassNotFoundException e) {}

        return type;
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
