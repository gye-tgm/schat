package com.example.SChat;

import android.app.Activity;
import android.os.Bundle;

public class Activity_MainMenu extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        loadContacts();
    }

    /**
     * Loads all saved contacts into the contact-menu-list.
     */
    private void loadContacts() {

    }
}
