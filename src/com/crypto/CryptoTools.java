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

    public static <C extends Content> Message<C> decryptMessage(SignedObject secureMessage, SecretKey key, PublicKey verificationKey) {
        Message<C> message = null;

        try {
            if(secureMessage.verify(verificationKey, Cryptography.getSignature())) {
                SecureMessage verifiedMessage = (SecureMessage)secureMessage.getObject();
                message = verifiedMessage.<C>decrypt(key);
            }
        }
        /* catch(InvalidKeyException e) {}
        catch(SignatureException e) {}
        catch(ClassNotFoundException e) {}
        catch(IOException e) {} */
        catch(Exception e) {
            e.printStackTrace();
        }

        return message;
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
