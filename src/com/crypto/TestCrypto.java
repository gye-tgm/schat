package com.crypto;

import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.SignedObject;
import java.util.Calendar;

import com.data.ChatContent;
import com.data.ChatMessage;
import com.data.Message;
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
        KeyPair keypair = Cryptography.gen_asymm_key();

        Message<ChatContent> message = new Message<>(Calendar.getInstance().getTime(), "Alice", "Bob", new ChatContent("This is a Test"));
        SignedObject secure_message = CryptoTools.encryptMessage(message, skey, keypair.getPrivate());
        System.out.println(CryptoTools.<ChatContent>decryptMessage(secure_message, skey, keypair.getPublic()));
    }

}
