package com.crypto;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.*;

/**
 * This class contains all essential methods for en- and decryption.
 * @author Elias Frantar (0.3)
 * @version 2.11.2013 (0.3)
 */
public class Cryptography {

    /**
     * En- or decrypts the given data using the in CryptoConstants specified encryption algorithm.
     * @param key the symmetric key
     * @param iv the initialization vector for the used operating mode
     * @param data the data to en- or decrypt
     * @param cipherMode en- or decryption
     * @return the en- or decrypted data
     */
    public static byte[] symm_crypt(SecretKey key, IvParameterSpec iv, byte[] data, int cipherMode) {

        byte[] encrypted_data = null;

        try {
            Cipher cipher = Cipher.getInstance(CryptoConstants.symm_alg + "/" + CryptoConstants.symm_mode + "/" + CryptoConstants.symm_padding);
            cipher.init(cipherMode, key, iv);
            encrypted_data = cipher.doFinal(data);
        }
        catch(Exception e) {
        }

        return encrypted_data;
    }

    /**
     * Encrypts the given data using the in CryptoConstants specified asymmetric encryption algorithm.
     * @param key the public key
     * @param data the data
     * @return the encrypted data
     */
    public static byte[] asymm_encrypt(PublicKey key, byte[] data) {

        byte[] encrypted_data = null;

        try {
            Cipher cipher = Cipher.getInstance(CryptoConstants.asymm_Alg);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            encrypted_data = cipher.doFinal(data);
        }
        catch(Exception e) {
        }

        return encrypted_data;
    }

    /**
     * Decrypts the given data using the in CryptoConstants specified asymmetric encryption algorithm.
     * @param key the private key
     * @param data the data
     * @return the decrypted data
     */
    public static byte[] asymm_decrypt(PrivateKey key, byte[] data) {

        byte[] encrypted_data = null;

        try {
            Cipher cipher = Cipher.getInstance(CryptoConstants.asymm_Alg);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            encrypted_data = cipher.doFinal(data);
        }
        catch(Exception e) {
        }

        return encrypted_data;
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

    /**
     * Generates the Message Authentication Code of the given data using the in CryptoConstants specified Mac-algorithm.
     * @param key the key
     * @param data the data
     * @return the mac of the given data
     */
    public static byte[] mac(SecretKey key, byte[] data) {

        byte[] auth = null;

        try {
            Mac mac = Mac.getInstance(CryptoConstants.MAC_alg);
            mac.init(key);
            auth = mac.doFinal(data);
        }
        catch(Exception e) {
        }

        return auth;
    }

    /**
     * Randomly generates a keypair (public and private key) for the in CryptoConstants specified asymmetric algorithm.
     * @return the keypair
     */
    public static KeyPair gen_asymm_key() {

        KeyPair keypair = null;

        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(CryptoConstants.asymm_Alg);
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

        return iv;
    }

}
