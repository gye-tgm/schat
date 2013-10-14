package com.activities;

import android.content.Context;
import android.widget.TextView;
import com.model.User;

/**
 * @version 7.10.2013
 */
public class ContactView extends TextView {
    private User user;

    public ContactView(Context context, User user) {
        super(context);

        setText(user.getName());
    }
}
