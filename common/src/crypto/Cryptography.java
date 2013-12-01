package crypto;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * This class contains all essential methods for en- and decryption.
 * @author Elias Frantar
 * @version 30.11.2013
 */
public class Cryptography {

    /* encryption utilities */

    /**
     * Returns a symmetric Cipher with the properties described in CryptoConstants to use for sealing messages.
     * @return an instance of a symmetric cipher
     */
    public static Cipher getSymmCipher() {
        Cipher c = null;

        try {
            c = Cipher.getInstance(CryptoConstants.symm_alg + "/" + CryptoConstants.symm_mode + "/" + CryptoConstants.symm_padding);
        }
        /* both Exception should never be thrown (we only use valid algorithms and padding), so we don't handle them */
        catch(NoSuchAlgorithmException e) {}
        catch(NoSuchPaddingException e) {}

        return c;
    }

    /**
     * Returns an instance of an asymmetric signature-algorithm with the properties described in CryptoConstants to use for signing messages.
     * @return an instance of a signature-algorithm
     */
    public static Signature getSignature() {
        Signature s = null;

        try {
            s = Signature.getInstance(CryptoConstants.signature_alg);
        }
        catch (NoSuchAlgorithmException e) {}

        return s;
    }

    /**
     * Encrypts/wraps the given key using the in CryptoConstants specified asymmetric encryption algorithm.
     * @param key the public key
     * @param skey the key to wrap
     * @return the encrypted data
     */
    public static byte[] wrap(PublicKey key, SecretKey skey) {

        byte[] wrapped_key = null;

        try {
            Cipher cipher = Cipher.getInstance(CryptoConstants.asymm_alg);
            cipher.init(Cipher.WRAP_MODE, key);
            wrapped_key = cipher.wrap(skey);
        }
        catch(Exception e) {
        }

        return wrapped_key;
    }

    /**
     * Decrypts/Unwraps the given data using the in CryptoConstants specified asymmetric encryption algorithm.
     * @param key the private key
     * @param skey the wrapped SecretKey
     * @return the unwrapped SecretKey
     */
    public static SecretKey unwarp(PrivateKey key, byte[] skey) {

        Key unwrapped_key = null;

        try {
            Cipher cipher = Cipher.getInstance(CryptoConstants.asymm_alg);
            cipher.init(Cipher.UNWRAP_MODE, key);
            unwrapped_key = cipher.unwrap(skey, CryptoConstants.symm_alg, Cipher.SECRET_KEY);
        }
        catch(Exception e) {
        }

        return (SecretKey)unwrapped_key;
    }

    /**
     * Computes the message digest of the given data using the in CryptoConstants specified algorithm.
     * @param data the data
     * @return the digest of the data
     */
    public static byte[] digest(byte[] data) {

        byte[] hash = null;

        try {
            MessageDigest digest = MessageDigest.getInstance(CryptoConstants.digest_alg);
            hash = digest.digest(data);
        }
        catch(Exception e) {
        }

        return hash;
    }

    /* key and IV-generation */

    /**
     * Randomly generates a keypair (public and private key) for the in CryptoConstants specified asymmetric algorithm.
     * @return the keypair
     */
    public static KeyPair gen_asymm_key() {

        KeyPair keypair = null;

        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(CryptoConstants.asymm_alg);
            generator.initialize(CryptoConstants.asymm_keylength);
            keypair = generator.generateKeyPair();
        }
        catch(Exception e) {
        }

        return keypair;
    }

    /**
     * Randomly generates a key for the in CryptoConstants specified symmetric algorithm.
     * @return the key
     */
    public static SecretKey gen_symm_key() {

        SecretKey key = null;

        try {
            KeyGenerator generator = KeyGenerator.getInstance(CryptoConstants.symm_alg);
            generator.init(CryptoConstants.symm_keylength);
            key = generator.generateKey();
        }
        catch(Exception e) {
        }

        return key;
    }

    /**
     * Generates a new Initialization Vector for the in CryptoConstants specified symmetric algorithm.
     * @return the IV
     */
    public static IvParameterSpec gen_symm_IV() {

        IvParameterSpec iv = null;

        try {
            SecureRandom random = new SecureRandom();
            byte iv_bytes[] = new byte[16];//generate random 16 byte IV AES is always 16bytes
            random.nextBytes(iv_bytes);
            iv = new IvParameterSpec(iv_bytes);
        }
        catch(Exception e) {
        }

        return iv;
    }

    /* create key-Objects from byte[]s */

    public static SecretKey getSecretKeyFromBytes(byte[] bytes) {
        SecretKey key = null;

        key = new SecretKeySpec(bytes, CryptoConstants.symm_alg);

        return key;
    }

    /**
     * Generate a public key from the given bytes.
     * @param bytes the bytes represent the public key.
     * @return the public key from the given bytes
     */
    public static PublicKey getPublicKeyFromBytes(byte[] bytes){
        PublicKey publicKey = null;
        try {
            publicKey = KeyFactory.getInstance(CryptoConstants.asymm_alg).generatePublic(new X509EncodedKeySpec(bytes));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return publicKey;
    }

    /* for testing only! */

    /**
     * Only use for testing!
     * Fenerates a keypair from the given seed (public and private key) for the in CryptoConstants specified asymmetric algorithm.
     * @param seed the seed for the secure prng
     * @return the keypair
     */
    public static KeyPair gen_asymm_key(byte[] seed) {

        KeyPair keypair = null;

        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(CryptoConstants.asymm_alg);
            generator.initialize(CryptoConstants.asymm_keylength, new SecureRandom(seed));
            keypair = generator.generateKeyPair();
        }
        catch(Exception e) {
        }

        return keypair;
    }
    /**
     * Generates a key from the given seed for the in CryptoConstants specified symmetric algorithm.
     * @param seed the seed for the secure prng
     * @return the key
     */
    public static SecretKey gen_symm_key(byte[] seed) {

        SecretKey key = null;

        try {
            KeyGenerator generator = KeyGenerator.getInstance(CryptoConstants.symm_alg);
            generator.init(CryptoConstants.symm_keylength, new SecureRandom(seed));
            key = generator.generateKey();
        }
        catch(Exception e) {
        }

        return key;
    }

}
