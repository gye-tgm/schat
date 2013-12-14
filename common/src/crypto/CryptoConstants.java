package crypto;

/**
 * This class contains all decisions regarding encryption and decryption algorithms and key sizes.
 * @version 30.11.2013
 * @author Elias Frantar
 */
public class CryptoConstants {

    public static final String symm_alg = "AES";
    public static final String symm_mode = "CTR";
    public static final String symm_padding = "NoPadding";
    public static final int symm_keylength = 128;

    public static final String asymm_alg = "RSA";
    public static final String asymm_mode = "NONE";
    public static final String asymm_padding = "PKCS1Padding";
    public static final int asymm_keylength = 2048;

    public static final String signature_alg = "SHA256withRSA";

    public static final String digest_alg = "SHA256";

}
