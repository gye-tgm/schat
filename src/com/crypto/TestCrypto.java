package com.crypto;

import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.security.KeyPair;
import java.util.Calendar;

import com.data.ChatMessage;
import com.data.User;

/**
 *
 */
public class TestCrypto {

    public static void main(String[] args) {

        String text = "This is a simple Test!";

        User sender = new User("Alice");
        User receiver = new User("Bob");

        SecretKey skey = Cryptography.gen_symm_key();
        SecretKey mackey = Cryptography.gen_MAC_key();
        KeyPair keypair = Cryptography.gen_asymm_key();

    }


}
