package com.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.crypto.PRNGFixes;
import com.data.User;

import java.util.ArrayList;
import java.util.Arrays;

/* todo: add options menu handling */

/**
 * The main activity of the S/Chat-Application. It displays and manages the list of all available contacts.
 *
 * @author Elias Frantar (0.1)
 * @version 12.10.2013: 0.1
 */
public class Activity_ContactList extends Activity {
    private ListView contactList; // the GUI element

    private ArrayList<String> contacts; // the stored contacts
    private ArrayAdapter<String> contactsAdapter; // to automatically update the ListView with onDataSetChanged
    private Intent start_chat;
    private Context context;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        PRNGFixes.apply(); // apply all PRG security fixes

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_contactlist);
        context = this;

        //Test Intent Chat
        //Intent i = new Intent(this, Activity_Chat.class);
        //startActivity(i);

        /* make all GUI-element available */
        contactList = (ListView) findViewById(R.id.view_contactlist);
        registerForContextMenu(contactList); // register all list items for the context menu

        contacts = new ArrayList<String>();
        contactsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contacts); // simple_List_item_1 is the android default
        contactList.setAdapter(contactsAdapter); // set the data of the list

        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                start_chat = new Intent(context, Activity_Chat.class);
                User tmp = new User(arg2, contacts.get(arg2));
                start_chat.putExtra("notyou", tmp);
                startActivity(start_chat);
            }

        });

        loadContacts(); // load all contacts into the list
    }

    /**
     * Handels the ContextMenu actions.
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo(); // the item whose context menu was called

        switch (item.getItemId()) {
            case R.id.option_deleteContact:
                deleteContact(info.position); // delete the selected contact
                return true;
            case R.id.option_editContact: // edit the selected contact
                /* todo: implement contact editing */
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
    private void deleteContact(int contactIndex) {

        /* todo: maybe add animation */

        contacts.remove(contactIndex);
        contactsAdapter.notifyDataSetChanged();
    }

    /**
     * Loads all saved contacts into the contact-menu-list.
     */
    private void loadContacts() {

        /* todo: replace test loading with actual contacts from User-objects */
        /* todo: maybe add the custom views (to display additional information, like: new Message, date of last conversation, ...) */

        String[] users = new String[20];
        for (int i = 0; i < users.length; i++)
            users[i] = "User" + i;

        contacts.addAll(Arrays.asList(users));
        contactsAdapter.notifyDataSetChanged();
    }

    /**
     * Creates the OptionsMenu of this Activity
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contactlist, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Creates the ContextMenu of an individual List item
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        /* set the name of the selected item as the header title */
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(contacts.get(info.position));

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contact, menu);
    }

}
