package com.activities;

import android.app.Activity;
import android.content.ClipData;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.data.ChatAdapter;
import data.Message;
import data.User;
import data.contents.ChatContent;
import networking.SChatClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Wolfram Soyka
 * @version 14.10.13: 0.2
 */
public class Activity_Chat extends Activity {
    private ListView messageList;
    private ArrayList<Message<ChatContent>> messages; // the stored messages and timestamps
    private ChatAdapter messagesAdapter; // to automatically update the ListView with onDataSetChanged
    private User you, notyou;
    private ImageButton button_send;
    private Animation send_success, send_fail, send_all_fail;
    private LinearLayout lin;
    private boolean buttonOnly;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat);

        //Get Users
        you = new User("Wolfram"); //Get from.. shared pref?
        notyou = (User) getIntent().getSerializableExtra("notyou");

        //Set title
        setTitle(getString(R.string.chat_with) + " " + notyou.getName());

        //Getting Resources
        buttonOnly = false; //Get from res, make editable in settings
        button_send = (ImageButton) findViewById(R.id.send);
        send_success = AnimationUtils.loadAnimation(this, R.anim.send_success);
        send_fail = AnimationUtils.loadAnimation(this, R.anim.send_fail);
        send_all_fail = AnimationUtils.loadAnimation(this, R.anim.send_all_fail);
        lin = (LinearLayout) findViewById(R.id.layout_chat_linlay);

        //Setting up List and Adapter
        messageList = (ListView) findViewById(R.id.view_chat);
        messages = new ArrayList<>();
        messagesAdapter = new ChatAdapter(this, messages, you.getName());
        messageList.setAdapter(messagesAdapter); // set the data of the list

        //Hide keyboard when scrolling
        messageList.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

        //Context menu
        registerForContextMenu(messageList);

        //Load Previous Messages
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
                copyText(info.position); // copy selected messages text
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Deletes the Message at given index.
     *
     * @param index the index of the message in the list
     */
    private void deleteMessage(int index) {
        /* todo: add animation */
        messages.remove(index);
        messagesAdapter.notifyDataSetChanged();
    }

    /**
     * Loads all saved Messages into the Messages-list.
     */
    private void loadMessages() {
        /* todo: replace test loading with actual messages */
        Random r = new Random();
        String alphabet = "ab cd ef gh ij kl mn op qe rs tu vw xy zö üä";
        for (int j = 0; j < 100; j++) {
            String print = "";
            int size = (int) (Math.random() * 100 + 1);
            for (int i = 0; i < size; i++)
                print += (alphabet.charAt(r.nextInt(alphabet.length())));
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

    /**
     * Checks if the Message is empty, starts animation and calls sendMessage()
     *
     * @param v the pressed Button
     */
    @SuppressWarnings("unused")
    public void sendMessagePressed(View v) {
        EditText text = (EditText) findViewById(R.id.eingabe);
        String tmp = text.getText().toString().trim();
        if (tmp.equals("")) {
            if (!buttonOnly)
                lin.startAnimation(send_all_fail);
            else
                button_send.startAnimation(send_fail);
        } else {
            if (!buttonOnly)
                lin.startAnimation(send_success);
            else
                button_send.startAnimation(send_success);
            sendMessage(tmp);
            text.setText("");
        }
    }

    /**
     * Adds a Message to local messages, updates the View and scrolls to bottom of the list.
     *
     * @param text New Messages Text
     */
    public void sendMessage(String text) {
        messages.add(new Message<ChatContent>(Calendar.getInstance().getTime(), you.getName(), notyou.getName(), new ChatContent(text)));
        messagesAdapter.notifyDataSetChanged();
        messageList.setSelection(messagesAdapter.getCount() - 1);
    }

    /**
     * Used to copy a Messages Text
     *
     * @param index index of the message to be copied
     */
    public void copyText(int index) {
        android.content.ClipboardManager clipboard =  (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("S/Chat", messages.get(index).getContent().getMessage());
        clipboard.setPrimaryClip(clip);
        clipboard.setPrimaryClip(clip);
    }
}
