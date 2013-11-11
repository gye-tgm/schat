package com.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import com.data.ChatAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Wolfram
 * Date: 14.10.13
 * Time: 13:04
 */
public class Activity_Chat extends Activity {
    private ListView messageHistory;
    private ArrayList<String> messages; // the stored messages
    private ArrayAdapter<String> messagesAdapter; // to automatically update the ListView with onDataSetChanged

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat);
        messageHistory = (ListView) findViewById(R.id.view_chat);
        messages = new ArrayList<String>();
        //messagesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messages); // simple_List_item_1 is the android default
        messagesAdapter = new ChatAdapter(this, messages);
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
                deleteMessage(info.position); // delete the selected contact
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Deletes the given Message.
     *
     * @param messageIndex the index of the contact in the list
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

        String[] texts = new String[20];
        Random r = new Random();
        String alphabet = "abcdefghijklmonpqestuvwxyzöüä";
        for (int j = 0; j < texts.length; j++) {
            int saize = (int) (Math.random() * 100 + 1);
            String print = "";
            for (int i = 0; i < saize; i++) {
                print += (alphabet.charAt(r.nextInt(alphabet.length())));
            }
            texts[j] = print;
        }
        messages.addAll(Arrays.asList(texts));
        messagesAdapter.notifyDataSetChanged();

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

    public void sendMessage(View v) {
        EditText text = (EditText) findViewById(R.id.eingabe);
        messages.add(text.getText().toString());
        text.setText("");
        messagesAdapter.notifyDataSetChanged();
        messageHistory.setSelection(messagesAdapter.getCount() - 1);
    }
}