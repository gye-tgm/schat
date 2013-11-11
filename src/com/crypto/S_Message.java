package com.crypto;

import com.data.User;

import java.math.BigInteger;
import java.util.Calendar;

/**
 *
 */
public class S_Message {

    /* plain */
    private Calendar timestamp;
    private User sender;
    private User receiver;

    /**
     * Converts a byte[] to a hex-String
     * @param bytes the byte[]to convert
     * @return the corresponding hex-String
     */
    public static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }
}
