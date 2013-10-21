package com.crypto;

/**
 * This class contains all decisions regarding encryption and decryption algorithms and key sizes.
 * @version 21.10.2013: 0.1
 * @author Elias Frantar (0.1)
 */
public class CryptoConstants {

    public static final String symm_alg = "AES";
    public static final String symm_mode = "CTR";
    public static final int symm_keylength = 256;
    public static final int symm_blocksize = 128;

    public static final String asymm_Alg = "RSA";
    public static final int asymm_keylength = 2048;

    public static final String MAC_Alg = "HmacSHA256";
    public static final int MAC_keylength = 128;
    public static final int MAC_length = 128;

}
