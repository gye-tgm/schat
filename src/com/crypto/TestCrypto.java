package com.crypto;

import javax.crypto.SecretKey;
import java.math.BigInteger;
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

        ChatMessage message = new ChatMessage(sender, receiver, text);
        message.setActualTime();
        S_ChatMessage secret_message = new S_ChatMessage(message, skey);

        System.out.println("\n");
        System.out.print(secret_message.toString());

        try {
            Envelope envelope = new Envelope(secret_message, mackey);

            System.out.println("Message Authentication Code: " + S_Message.toHex(envelope.getSignature()) + "\n");

            if(envelope.verifyMAC(mackey)) {
                System.out.println("Plaintext: " + ((S_ChatMessage)(envelope.getMessage())).decrypt(skey).toString());
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Converts a byte[] to a hex-String
     * @param bytes the byte[]to convert
     * @return the corresponding hex-String
     */
    private static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }

}
