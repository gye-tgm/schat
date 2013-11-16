package com.activities;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
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
    private ListView messageList;
    private ArrayList<ChatMessage> messages; // the stored messages and timestamps
    private ChatAdapter messagesAdapter; // to automatically update the ListView with onDataSetChanged
    private User you, notyou;
    private ImageButton button_send;
    private Animation send_anim, send_fail, send_all_fail;
    private LinearLayout lin;
    private boolean buttonOnly;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat);
        //Set title
        notyou = (User) getIntent().getSerializableExtra("notyou");
        setTitle(getString(R.string.chat_with) + " " + notyou.getName());
        //Getting Resources
        buttonOnly = false; //Get from res, editable in settings
        button_send = (ImageButton) findViewById(R.id.send);
        send_anim = AnimationUtils.loadAnimation(this, R.anim.send_animation);
        send_fail = AnimationUtils.loadAnimation(this, R.anim.send_fail);
        send_all_fail = AnimationUtils.loadAnimation(this, R.anim.send_all_fail);
        lin = (LinearLayout) findViewById(R.id.layout_chat_linlay);
        //Users
        you = new User("Wolfram");
        //notyou = new User("Gary");
        //Setting up List
        messageList = (ListView) findViewById(R.id.view_chat);
        messages = new ChatArrayList();
        messagesAdapter = new ChatAdapter(this, messages, you);
        messageList.setAdapter(messagesAdapter); // set the data of the list
        registerForContextMenu(messageList); // register all list items for the context menu
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
     * Deletes the given Message.
     *
     * @param index the index of the message in the list
     */
    private void deleteMessage(int index) {
        /* todo: maybe add animation */
        messages.remove(index);
        messagesAdapter.notifyDataSetChanged();
    }

    /**
     * Loads all saved Messages into the Messages-list.
     */
    private void loadMessages() {
        /* todo: replace test loading with actual messages from User-objects */
        Random r = new Random();
        String alphabet = "abcdefghijklmonpqestuvwxyzöüä         ";
        for (int j = 0; j < 100; j++) {
            String print = "";
            int saize = (int) (Math.random() * 100 + 1);
            for (int i = 0; i < saize; i++)
                print += (alphabet.charAt(r.nextInt(alphabet.length())));
            testSendMessage(new ChatMessage(you, notyou, print));
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
     * Checks if the Message is empty, if so shows a Toast, else sends a Message via sendMessage() and animates the button
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
                lin.startAnimation(send_anim);
            else
                button_send.startAnimation(send_anim);
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
        messages.add(new ChatMessage(you, notyou, text));
        messagesAdapter.notifyDataSetChanged();
        messageList.setSelection(messagesAdapter.getCount() - 1);
    }

    /**
     * Used for Testing only, user has no access to this
     * Sends a message with defined ChatMessage
     *
     * @param newChatMessage the ChatMessage to be sent
     */
    public void testSendMessage(ChatMessage newChatMessage) {
        messages.add(newChatMessage);
        messagesAdapter.notifyDataSetChanged();
        messageList.setSelection(messagesAdapter.getCount() - 1);
    }

    /**
     * Used to copy a Messages Text
     *
     * @param index index of the message to be copied
     */
    public void copyText(int index) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("S/Chat", messages.get(index).getMessage());
        clipboard.setPrimaryClip(clip);
    }
}