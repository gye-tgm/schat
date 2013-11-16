package com.crypto;

import com.data.Message;

import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.SignedObject;

/**
 * This class provides an easy interface for message encryption and decryption, which automatically handles the different classes
 * required for en- and decryption. It also provides some other crypto-utilities.
 * @author Elias Frantar
 * @version 16.11.2013
 */
public class CryptoTools {

    public static SignedObject encryptMessage(Message message, SecretKey key, PrivateKey privateKey) {

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
