package com.data;

import java.util.ArrayList;

/**
 * Attempt to trick ChatAdapter.java's constructors super() method
 * User: Wolfram
 * Date: 12.11.13
 * Time: 23:36
 */
public class ChatArrayList extends ArrayList<ChatMessage> {
    public ArrayList<String> toStringArrayList() {
        ArrayList<String> ret = new ArrayList<>();
        for (int i = 0; i < this.size(); i++) {
            ret.add(this.get(i).getMessage());
        }
        return ret;
    }
}
