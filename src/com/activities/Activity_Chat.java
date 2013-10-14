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

import java.util.ArrayList;
import java.util.Arrays;

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
        messagesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messages); // simple_List_item_1 is the android default
        messageHistory.setAdapter(messagesAdapter); // set the data of the list

        registerForContextMenu(messageHistory); // register all list items for the context menu

        loadMessages();
    }

    /**
     * Handels the ContextMenu actions.
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
     * Deletes the given contact.
     *
     * @param contactIndex the index of the contact in the list
     */
    private void deleteMessage(int contactIndex) {

        /* todo: maybe add animation */

        messages.remove(contactIndex);
        messagesAdapter.notifyDataSetChanged();
    }

    /**
     * Loads all saved contacts into the contact-menu-list.
     */
    private void loadMessages() {

        /* todo: replace test loading with actual messages from User-objects */
        /* todo: maybe add the custom views (like: delete) */

        String[] texts = new String[20];
        for (int i = 0; i < texts.length; i++)
            texts[i] = "Message" + i;

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
    }
}