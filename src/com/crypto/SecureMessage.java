package com.crypto;

import com.data.Content;
import com.data.Message;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

/**
 * This is a secure Message which will be sent. It contains only sealed content.
 * @author Elias Frantar
 * @version 16.11.2013
 */
public class SecureMessage extends Message implements Serializable {

    private SealedObject sealedContent;

    /**
     * Creates a new SecureMessage from a plain message.
     * @param m the plain message
     * @param key the key to use for encrypting the content
     */
    public SecureMessage(Message m, SecretKey key) {
        super(m.getTimestamp(), m.getSender(), m.getReceiver());

        try {
            Cipher c = Cryptography.getSymmCipher();
            c.init(Cipher.ENCRYPT_MODE, key, Cryptography.gen_symm_IV());
            sealedContent = new SealedObject(m.getContent(), Cryptography.getSymmCipher());
        }
        catch(IllegalBlockSizeException e) {}
        catch(InvalidAlgorithmParameterException e) {}
        catch(InvalidKeyException e) {}
        catch(IOException e) {}
    }

    /**
     * Decrypts the content of this SecureMessage and returns the whole plain message to allow easier processing.
     * @param key the key for decryption
     * @return the plain message of this secure instance
     * @throws Exception if something went wrong, transmission was not successfull
     */
    public Message decrypt(SecretKey key) throws Exception {
        return new Message(timestamp, sender, receiver, (Content)sealedContent.getObject(key));
    }

    /**
     * Returns the content of this message in a properly formatted and readable String (encrypted parts are hex-encoded)
     * @return this SecureMessage as a String
     */
    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append(super.requiredInfoToString());
        s.append(CryptoTools.toHex(sealedContent.toString().getBytes()));

        return s.toString();
    }
}
