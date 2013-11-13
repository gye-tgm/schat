package com.crypto;

import com.data.ChatMessage;

import javax.crypto.SecretKey;
import java.io.IOException;

/**
 *  This class allows easy generation of encrypted envelopes.
 *  @author Elias Frantar
 *  @version 13.11.2013
 */
public class EnvelopeFactory {

    /**
     * Creates a new secure envelope containing a message ready to sent over the network.
     * @param message the plain message
     * @param symm_key the symmetric encryption key
     * @param mac_key the MAC key
     * @return an envelope containing the encrypted message
     */
    public static Envelope createEnvelope_ChatMessage(ChatMessage message, SecretKey symm_key, SecretKey mac_key) throws IOException {
        return new Envelope(new S_ChatMessage(message, symm_key), mac_key);
    }

    /**
     * Returns the plain message contained in the given envelope.
     * @param envelope the envelope containing the message to decrypt
     * @param symm_key the symmetric encryption key
     * @param mac_key the MAC key
     * @return the plain message
     */
    public static ChatMessage openEnvelope(Envelope envelope, SecretKey symm_key, SecretKey mac_key) {
        ChatMessage plainmessage = null;

        try {
            if(envelope.verifyMAC(mac_key))
                plainmessage = ((S_ChatMessage)(envelope.getMessage())).decrypt(symm_key);
        }
        catch(Exception e) {
        }

        return  plainmessage;
    }
}
