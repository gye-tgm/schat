package com.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.data.AndroidSQLManager;
import com.security.PRNGFixes;
import com.services.MessageService;
import data.User;

import java.util.ArrayList;

/* todo: add options menu handling */

/**
 * The main activity of the S/Chat-Application. It displays and manages the list of all available contacts.
 *
 * @author Wolfram Soyka (0.2)
 * @version 2.12.2013: 0.2
 */
public class Activity_ContactList extends Activity {
    private ListView contactList; // the GUI element

    private ArrayList<String> contacts; // the stored contacts
    private ArrayAdapter<String> contactsAdapter; // to automatically update the ListView with onDataSetChanged
    private Intent start_chat;
    private Context context;

    private Intent service;
    private AndroidSQLManager dbManager;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        PRNGFixes.apply(); // apply all PRG security fixes

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_contactlist);
        context = this;

        service = new Intent(getApplicationContext(), MessageService.class);
        startService(service);

        /* make all GUI-elements available */
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
                User tmp = new User(contacts.get(arg2));
                start_chat.putExtra("notyou", tmp);
                startActivity(start_chat);
            }

        });

        dbManager = new AndroidSQLManager();
        dbManager.connect();
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
     * Creates the OptionsMenu of this Activity
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_contactlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_addContact:
            add();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Deletes the contact at given index.
     *
     * @param contactIndex the index of the contact in the list
     */
    private void deleteContact(int contactIndex) {

        /* todo: maybe add animation */

        contacts.remove(contactIndex);
        contactsAdapter.notifyDataSetChanged();
    }

    /**
     * Adds a contact to the List
     */
    public void add(){
        final EditText txt = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Add Contact")
                .setView(txt)
                .setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                @SuppressWarnings("unchecked")
                                String newUser = txt.getText().toString();
                                if (!newUser.equals("")) {
                                    contactsAdapter.add(newUser);
                                    // Refreshes the content
                                    contactsAdapter.notifyDataSetChanged();
                                    // Shows toast
                                    Toast.makeText(context, "Added "+newUser +" to contacts.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // Do nothing. (Closes Dialog)
                            }
                        }).show();
    }

    /**
     * Loads all saved contacts into the contact-menu-list.
     */
    private void loadContacts() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("Alice", null, null));
        users.add(new User("Bob", null, null));
        users.add(new User("Eve", null, null));

        for(User u : users)
            dbManager.insertUser(u);

        users = dbManager.loadUsers();
        for(User u : users)
            contacts.add(u.getId());
    }

    @Override
    public void onDestroy() {
        dbManager.disconnect();
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
