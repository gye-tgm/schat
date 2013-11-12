package com.activities;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.data.ChatAdapter;
import com.data.ChatArrayList;
import com.data.ChatMessage;
import com.data.User;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Wolfram
 * Date: 14.10.13
 * Time: 13:04
 */
public class Activity_Chat extends Activity {
    private ListView messageHistory;
    private ChatArrayList messages; // the stored messages and timestamps
    private ArrayList<String> test; //for some reason required in ChatAdapter.java
    private ChatAdapter messagesAdapter; // to automatically update the ListView with onDataSetChanged
    private User you, notyou;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat);
        messageHistory = (ListView) findViewById(R.id.view_chat);
        messages = new ChatArrayList();
        test = messages.toStringArrayList();
        messagesAdapter = new ChatAdapter(this, messages, test);
        you = new User("Wolfram");
        notyou = new User("Gary");
        messageHistory.setAdapter(messagesAdapter); // set the data of the list
        registerForContextMenu(messageHistory); // register all list items for the context menu
        loadMessages();
    }

    /**
     * Handles the ContextMenu actions.
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo(); // the item whose context menu was called
        switch (item.getItemId()) {
            case R.id.option_deleteMessage:
                deleteMessage(info.position); // delete the selected message
                return true;
            case R.id.option_copyText:
                copyText(info.position);
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Deletes the given Message.
     *
     * @param messageIndex the index of the message in the list
     */
    private void deleteMessage(int messageIndex) {
        /* todo: maybe add animation */
        messages.remove(messageIndex);
        messagesAdapter.notifyDataSetChanged();
    }

    /**
     * Loads all saved Messages into the Messages-list.
     */
    private void loadMessages() {
        /* todo: replace test loading with actual messages from User-objects */
        /* todo: maybe add the custom views (like: delete) */
        Random r = new Random();
        String alphabet = "abcdefghijklmonpqestuvwxyzöüä";
        for (int j = 0; j < 20; j++) {
            int saize = (int) (Math.random() * 100 + 1);
            String print = "";
            ChatMessage dummy = null;
            for (int i = 0; i < saize; i++) {
                print += (alphabet.charAt(r.nextInt(alphabet.length())));
                dummy = (i % 2 == 0) ? new ChatMessage(you, notyou, print) : new ChatMessage(notyou, you, print);
            }
            testSendMessage(dummy);
        }
    }

    /**
     * Creates the ContextMenu of an individual List item
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_message, menu);
    }

    /**
     * Checks if the Message is empty, if so shows a Toast, else sends a Message via sendMessage()
     *
     * @param v ButtonView
     */
    public void sendMessagePressed(View v) {
        EditText text = (EditText) findViewById(R.id.eingabe);
        String tmp = text.getText().toString().trim();
        if (tmp.equals("")) {
            Toast.makeText(this, "No empty Messages", Toast.LENGTH_SHORT).show();
        } else {
            sendMessage(tmp);
            text.setText("");
        }
    }

    /**
     * Adds a Message to local messages, updates the View and scrolls to bottom of the list.
     * For some reason the test ArrayList is needed for the ChatAdapter.java
     *
     * @param text New Messages Text
     */
    public void sendMessage(String text) {
        messages.add(new ChatMessage(notyou, you, text));
        test.add(text);
        messagesAdapter.notifyDataSetChanged();
        messageHistory.setSelection(messagesAdapter.getCount() - 1);
    }

    /**
     * Used for Testing only, user has no acces to this
     * Sends a message with defined ChatMessage
     *
     * @param newChatMessage
     */
    public void testSendMessage(ChatMessage newChatMessage) {
        messages.add(newChatMessage);
        test.add(newChatMessage.getMessage());
        messagesAdapter.notifyDataSetChanged();
        messageHistory.setSelection(messagesAdapter.getCount() - 1);
    }

    /**
     * Used to copy a Messages Text
     *
     * @param pos
     */
    public void copyText(int pos) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("S/Chat", messages.get(pos).getMessage());
        clipboard.setPrimaryClip(clip);
    }
}