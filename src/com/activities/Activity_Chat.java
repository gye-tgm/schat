package com.activities;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.data.ChatAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Wolfram
 * Date: 14.10.13
 * Time: 13:04
 */
public class Activity_Chat extends Activity {
    private ListView messageHistory;
    private ArrayList<String> messages, timestamps; // the stored messages and timestamps
    private ArrayAdapter<String> messagesAdapter; // to automatically update the ListView with onDataSetChanged

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat);
        messageHistory = (ListView) findViewById(R.id.view_chat);
        messages = new ArrayList<>();
        timestamps = new ArrayList<>();
        messagesAdapter = new ChatAdapter(this, messages, timestamps);
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
            for (int i = 0; i < saize; i++) {
                print += (alphabet.charAt(r.nextInt(alphabet.length())));
            }
            sendMessage(print);
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

    public void sendMessagePressed(View v) {
        EditText text = (EditText) findViewById(R.id.eingabe);
        if (text.equals("")) {
            Toast.makeText(this, "No empty Messages", Toast.LENGTH_SHORT).show();
        } else {
            sendMessage(text.getText().toString().trim());
            text.setText("");
        }
    }

    public void sendMessage(String text) {
        messages.add(text);
        Calendar c = Calendar.getInstance();
        String min = "" + c.get(Calendar.MINUTE);
        if (Integer.parseInt(min) < 10) {
            min = "0" + min;
        }
        timestamps.add("" + c.get(Calendar.HOUR_OF_DAY) + ":" + min + " | " + c.get(Calendar.DATE) + "." + c.get(Calendar.MONTH));
        messagesAdapter.notifyDataSetChanged();
        messageHistory.setSelection(messagesAdapter.getCount() - 1);
    }

    public void copyText(int pos) {
        // TODO
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", messages.get(pos));
        clipboard.setPrimaryClip(clip);
    }
}