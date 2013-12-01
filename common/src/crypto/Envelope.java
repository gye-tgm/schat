package crypto;

import data.Content;
import data.Message;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.*;

/**
 * Instances of this class will be sent over the web. This class contains a signed and sealed message. It provides easier access to those attributes.
 * @author Elias Frantar
 * @version 29.11.2013
 */
public class Envelope implements Serializable {

    private SignedObject sign_sec_message;

    private transient SecureMessage sec_message; // this attribute will not be serialized

    /**
     * Generates a transmittable Envelope containing a secure signed and sealed Object.
     * @param message the message to seal and sign
     * @param key the shared symmetric key between sender and receiver (key for sealing)
     * @param signKey the private key of the sender (key for signing)
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
     * Generates a transmittable Envelope containing a secure signed and sealed Object including a header with the wrapped SecretKey.
     * @param message the message to seal and sign
     * @param key the symmetric key which will be shared between sender and receiver afterwards (key for sealing)
     * @param wrapKey the public key of the receiver (key to use for wrapping the SecretKey)
     * @param signKey the private key of the sender (key for signing)
     */
    public Envelope(Message message, SecretKey key, PublicKey wrapKey, PrivateKey signKey) {
        SecureMessage secureMessage = new SecureMessage(message, key, wrapKey);

        try {
            sign_sec_message = new SignedObject(secureMessage, signKey, Cryptography.getSignature());
        }
        catch(InvalidKeyException e) {}
        catch(SignatureException e) {}
        catch(IOException e) {}
    }

    /**
     * Decrypts the message contained in this envelope and returns the plain message of the specified type.
     * Warning: does not verify the signature! You must call verify() afterwards. Only use this method if its really necessary!
     * @param key the shared symmetric key between Sender and receiver (call getUnwrappedKey() to get it) (key for content decryption)
     * @param <C> the requested return type
     * @return the plain message of the requested type
     */
    public <C extends Content> Message<C> decryptMessage(SecretKey key) {
        deserializeIfNecessary();
        Message<C> message = null;

        try {
            message = sec_message.<C>decrypt(key);
        }
        catch(InvalidKeyException e) {}
        catch(Exception e) {}

        return message;
    }

    /**
     * Verifies the signature of the contained message.
     * @param verificationKey the public key to verfiy the message with
     * @return true if verified; false otherwise
     */
    public boolean verify(PublicKey verificationKey) {
        boolean verified = false;

        try {
            verified = sign_sec_message.verify(verificationKey, Cryptography.getSignature());
        }
        catch (InvalidKeyException e) {}
        catch (SignatureException e) {}

        return verified;
    }

    /**
     * Decrypts the message contained in this envelope and returns the plain message of the specified type. Also verifies the signature.
     * @param key the shared symmetric key between sender and receiver (key for content decryption decryption)
     * @param verificationKey the public key of the sender (key to verify the signature)
     * @param <C> the requested return type
     * @return the plain message of the requested type; null if the signature did not verify
     */
    public <C extends Content> Message<C> decryptMessage(SecretKey key, PublicKey verificationKey) {
        deserializeIfNecessary();
        Message<C> message = null;

        try {
            if(verify(verificationKey))
                message = sec_message.<C>decrypt(key);
        }
        catch(InvalidKeyException e) {}
        catch(Exception e) {}

        return message;
    }

    /**
     * Returns the type of the received message.
     * @return the type of the message contained in this envelope
     */
    public Content.Type getType() {
        deserializeIfNecessary();

        return sec_message.getContentType();
    }

    /**
     * Returns the unwrapped key contained in the message header.
     * @param key the private key of the receiver (key to decrypt the header)
     * @return the SecretKey contained in the header
     */
    public SecretKey getUnwrappedKey(PrivateKey key) {
        deserializeIfNecessary();

        return sec_message.decryptHeader(key);
    }

    /**
     * Returns the sender of the received message.
     * @return the sender of the message contained in this envelope
     */
    public String getSender() {
        deserializeIfNecessary();

        return sec_message.getSender();
    }

    /**
     * Returns the receiver of the received message.
     * @return the receiver of the message contained in this envelope
     */
    public String getReceiver() {
        deserializeIfNecessary();

        return sec_message.getReceiver();
    }

    /**
     * Returns true if this message contains a header.
     * @return true if yes, false otherwise
     */
    public boolean containsHeader() {
        return sec_message.containsHeader();
    }

    /**
     * Deserializes the contained SecretMessage if it has not already been deserialized.
     */
    private void deserializeIfNecessary() {
        if(sec_message == null) {
            try {
                sec_message = (SecureMessage)sign_sec_message.getObject();
            }
            catch (IOException e) {}
            catch (ClassNotFoundException e) {}
        }
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
