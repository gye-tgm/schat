package com.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;

/**
 *
 */
public class TestCrypto {

    public static void main(String[] args) {
        String message = "This is a simple Test if this program works!";
        String key = "aaaaaaaaaaaaaaaa";
        String iv = "bbbbbbbbbbbbbbbb";

        System.out.println("message: " + message);
        System.out.println("key: " + key);

        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

        byte[] encrypted = Cryptography.symm_crypt(skeySpec, ivspec, message.getBytes(), Cipher.ENCRYPT_MODE);

        System.out.println(new String(encrypted, Charset.defaultCharset()));

        byte[] decrypted = Cryptography.symm_crypt(skeySpec, ivspec, encrypted, Cipher.DECRYPT_MODE);

        System.out.println(new String(decrypted, Charset.defaultCharset()));
    }
}
