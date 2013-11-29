package com.data.contents;

import com.data.Content;

/**
 *
 */
public class ChatContent extends Content {

    private String message;

    public ChatContent(String message) {
        this.type = Type.CHAT_MESSAGE;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String toString() {
        return message;
    }
}
