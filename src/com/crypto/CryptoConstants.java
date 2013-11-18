package com.crypto;

/**
 * This class contains all decisions regarding encryption and decryption algorithms and key sizes.
 * @version 29.10.2013: 0.2
 * @author Elias Frantar (0.2)
 */
public class CryptoConstants {

    public static final String symm_alg = "AES";
    public static final String symm_mode = "CTR";
    public static final String symm_padding = "NoPadding";
    public static final int symm_keylength = 128;
    public static final int symm_blocksize = 128;

    public static final String asymm_alg = "RSA";
    public static final int asymm_keylength = 2048;

    public static final String signature_alg = "SHA256withRSA";

    public static final String digest_alg = "SHA256";
    public static final int digest_length = 256;

    public static final String MAC_alg = "HmacSHA256";
    public static final int MAC_keylength = 128;
    public static final int MAC_length = 128;

}
