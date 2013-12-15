package crypto;

import data.Content;
import data.Message;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * This is a secure Message which will be sent. It contains only sealed content.
 * @author Elias Frantar
 * @version 16.11.2013
 */
public class SecureMessage extends Message implements Serializable {

    private byte[] header;

    private byte[] iv;

    private SealedObject sealedContent;

    private Content.Type contentType;

    /**
     * Creates a new SecureMessage from a plain message.
     * @param m the plain message
     * @param key the key to use for encrypting the content
     */
    public SecureMessage(Message<? extends Content> m, SecretKey key) {
        super(m.getTimestamp(), m.getSender(), m.getReceiver());
        contentType = m.getContent().getType(); // the content type needs to be stored here because it must be accessible before decrypting the content
        header = null;

        try {
            Cipher c = Cryptography.getSymmCipher();
            iv = Cryptography.gen_symm_IV().getIV();
            c.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            sealedContent = new SealedObject(m.getContent(), c);
        }
        catch(IllegalBlockSizeException e) {}
        catch(InvalidAlgorithmParameterException e) {}
        catch(InvalidKeyException e) {}
        catch(IOException e) {}
    }

    /**
     * Creates a new SecureMessage from a plain message.
     * @param m the plain message
     * @param skey the key to use for encrypting the content
     */
    public SecureMessage(Message<? extends Content> m, SecretKey skey, PublicKey pkey) {
        super(m.getTimestamp(), m.getSender(), m.getReceiver());
        contentType = m.getContent().getType(); // the content type needs to be stored here because it must be accessible before decrypting the content
        header = null;

        try {
            Cipher c = Cryptography.getSymmCipher();
            iv = Cryptography.gen_symm_IV().getIV();
            c.init(Cipher.ENCRYPT_MODE, skey, new IvParameterSpec(iv));
            sealedContent = new SealedObject(m.getContent(), c);

            header = Cryptography.wrap(pkey, skey);
        }
        catch(IllegalBlockSizeException e) {}
        catch(InvalidAlgorithmParameterException e) {}
        catch(InvalidKeyException e) {}
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Decrypts the content of this SecureMessage and returns the whole plain message to allow easier processing.
     * @param key the key for decryption
     * @param <C> the type of the content which will be contained in the returned plain message. (You can find out the content of this class with getContentType())
     * @return the plain message of this secure instance
     * @throws Exception if something went wrong, transmission was not successful
     */
    public <C extends Content> Message<C> decrypt(SecretKey key) throws Exception {
        Message<C> message = null;

        try {
            // message = new Message<C>(timestamp, sender, receiver, (C)sealedContent.getObject(key));
            Cipher cipher = Cryptography.getSymmCipher();
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            message = new Message<C>(timestamp, sender, receiver, (C)sealedContent.getObject(cipher));
        }
        catch(Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            String s = errors.toString();
            System.out.println(s);
        }
        return message;
    }

    /**
     * Returns the unwrapped SecretKey contained in the header of this message.
     * @param key the PrivateKey to unwrap the wrapped SecretKey
     * @return the unwrapped SecretKey if this message contains a header; null otherwise
     */
    public SecretKey decryptHeader(PrivateKey key) {
        if(containsHeader())
            return Cryptography.unwarp(key, header);
        else
            return null;
    }

    /**
     * Returns the content of this message in a properly formatted and readable String (encrypted parts are hex-encoded)
     * @return this SecureMessage as a String
     */
    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append(super.requiredInfoToString());
        if(header != null)
            s.append("\n Header: " + Envelope.toHex(header) + "\n\n");
        s.append(Envelope.toHex(sealedContent.toString().getBytes()));

        return s.toString();
    }

    /**
     * Returns true if this message contains a header.
     * @return true if yes, false otherwise
     */
    public boolean containsHeader() {
        return(header != null)?true:false;
    }


    public Content.Type getContentType() {
        return contentType;
    }
}
