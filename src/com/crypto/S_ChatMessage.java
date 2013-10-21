package com.crypto;

import com.data.ChatMessage;

import javax.crypto.SecretKey;
import java.io.Serializable;

/**
 *
 */
public class S_ChatMessage implements Serializable {

    private ChatMessage message;
    private String encrypted_message;
    private String tag;

    public S_ChatMessage(ChatMessage message, SecretKey sym_key, SecretKey MAC_key) {
        encrypt(sym_key, MAC_key);
    }

    public void encrypt(SecretKey sym_key, SecretKey MAC_key) {

    }

    public ChatMessage decrypt() {
          ChatMessage plainMessage = null;

          return plainMessage;
    }

    public void setEncrypted_message(String encrypted_message) {
        this.encrypted_message = encrypted_message;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getEncrypted_message() {
        return encrypted_message;
    }

    public String getTag() {
        return tag;
    }

    public void setMessage(ChatMessage message) {
        this.message = message;
    }

    public ChatMessage getMessage() {
        return message;
    }
}
