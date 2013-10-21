package com.crypto;

import javax.crypto.*;
import java.security.NoSuchAlgorithmException;

/**
 *
 */
public class Cryptography {

    public static byte[] symm_encrypt(SecretKey key, byte[] data) {

        try {
            Cipher cipher = Cipher.getInstance(CryptoConstants.symm_alg + "/" + CryptoConstants.symm_mode);
        }
        catch(Exception e) {

        }

        return null;
    }

}
