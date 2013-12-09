package com.data;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import crypto.Cryptography;
import data.DatabaseManager;
import data.Message;
import data.User;
import data.contents.ChatContent;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 */
public class AndroidSQLManager implements DatabaseManager {
    private final String DB_NAME = "clientdatabase";

    private final String CREATE_SCRIPT = "CREATE TABLE IF NOT EXISTS user (id TEXT, public_key BLOB, symmetric_key BLOB, PRIMARY KEY(id)); " +
                                         "CREATE TABLE IF NOT EXISTS message (sender_id TEXT, receiver_id TEXT, timestamp INTEGER, content TEXT, PRIMARY KEY(sender_id, receiver_id, timestamp));";

    private final String USER = "user";
    private final String ID = "id";
    private final String PUB_KEY = "public_key";
    private final String SYMM_KEY = "symmetric_key";

    private final String MESSAGE = "message";
    private final String SENDER = "sender";
    private final String RECEIVER = "receiver";
    private final String TIMESTAMP = "timestamp";
    private final String CONTENT = "content";

    SQLiteDatabase db;

    public void connect(Activity activity) {
        db = activity.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        createTables(CREATE_SCRIPT);
    }
    public void disconnect() {
        db.close();
    }

    @Override
    public void createTables(String createScriptFile) {
        db.execSQL(createScriptFile);
    }

    @Override
    public User getUserFromGivenId(String id) {
        User user = null;

        Cursor c = db.query(USER, null, ID + " = ?", new String[]{id}, null, null, null); // SELECT * FROM user WHERE id = ?; ? = id
        if(c.getCount() != 0) {
            PublicKey pub_key = Cryptography.getPublicKeyFromBytes(c.getBlob(c.getColumnIndex(PUB_KEY)));
            SecretKey symm_key = Cryptography.getSecretKeyFromBytes(c.getBlob(c.getColumnIndex(SYMM_KEY)));
            user = new User(id, new KeyPair(pub_key, null), symm_key);
        }

        return user;
    }

    @Override
    public PublicKey getPublicKeyFromId(String id) {
        PublicKey key = null;

        Cursor c = db.query(USER, new String[]{PUB_KEY}, ID + " = ?", new String[]{id}, null, null, null); // SELECT public_key FROM user WHERE id = ?; ? = id
        if(c.getCount() != 0)
            key = Cryptography.getPublicKeyFromBytes(c.getBlob(1));

        return key;
    }

    @Override
    public boolean userExists(String id) {
        return (getUserFromGivenId(id) != null);
    }

    @Override
    public void insertUser(User user) {
        String query = "INSERT INTO " + USER + " VALUES (?, ?, ?);";
        SQLiteStatement st = db.compileStatement(query);
        st.bindString(1, user.getId());
        st.bindBlob(2, user.getPublicKey().getEncoded());
        st.bindBlob(3, user.getSecretKey().getEncoded());

        st.executeInsert();
    }

    @Override
    public ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<>();

        Cursor c = db.query(USER, null, null, null, null, null, null); // SELECT * FROM user;
        while(c.moveToNext()) {
            String id = c.getString(c.getColumnIndex(ID));
            PublicKey pub_key = Cryptography.getPublicKeyFromBytes(c.getBlob(c.getColumnIndex(PUB_KEY)));
            SecretKey symm_key = Cryptography.getSecretKeyFromBytes(c.getBlob(c.getColumnIndex(SYMM_KEY)));
            users.add(new User(id, new KeyPair(pub_key, null), symm_key));
        }

        return users;
    }

    @Override
    public boolean removeUser(String id) {
        String query = "DELETE FROM " + USER + " WHERE id = ?;";
        SQLiteStatement st = db.compileStatement(query);
        st.bindString(1, id);

        return (st.executeUpdateDelete() > 0);
    }

    @Override
    public void insertMessage(Message<ChatContent> chatContentMessage) {
        String query = "INSERT INTO " + MESSAGE + " VALUES (?, ?, ?, ?);";
        SQLiteStatement st = db.compileStatement(query);
        st.bindString(1, chatContentMessage.getSender());
        st.bindString(2, chatContentMessage.getReceiver());
        st.bindLong(3, chatContentMessage.getTimestamp().getTime() / 1000);
        st.bindString(4, chatContentMessage.getContent().getMessage());

        st.executeInsert();
    }

    @Override
    public ArrayList<Message<ChatContent>> loadMessagesFromReceiver(String id) {
        ArrayList<Message<ChatContent>> messages = new ArrayList<>();

        Cursor c = db.query(MESSAGE, null, RECEIVER + " = ?", new String[]{id}, null, null, null); // SELECT * FROM message WHERE receiver = ?; ? = id
        while(c.moveToNext()) {
            String sender = c.getString(c.getColumnIndex(SENDER));
            String content = c.getString(c.getColumnIndex(CONTENT));
            Date timestamp = new Date(c.getLong(c.getColumnIndex(TIMESTAMP)));
            messages.add(new Message<ChatContent>(timestamp, sender, id, new ChatContent(content)));
        }

        return messages;
    }

    @Override
    public ArrayList<Message<ChatContent>> loadMessagesFromSender(String id) {
        ArrayList<Message<ChatContent>> messages = new ArrayList<>();

        Cursor c = db.query(MESSAGE, null, SENDER + " = ?", new String[]{id}, null, null, null); // SELECT * FROM message WHERE sender = ?; ? = id
        while(c.moveToNext()) {
            String receiver = c.getString(c.getColumnIndex(RECEIVER));
            String content = c.getString(c.getColumnIndex(CONTENT));
            Date timestamp = new Date(c.getLong(c.getColumnIndex(TIMESTAMP)));
            messages.add(new Message<ChatContent>(timestamp, id, receiver, new ChatContent(content)));
        }

        return messages;
    }

    public ArrayList<Message<ChatContent>> loadChat(String id) {
        ArrayList<Message<ChatContent>> messages = new ArrayList<>();

        Cursor c = db.query(MESSAGE, null, SENDER + " = ? OR " + RECEIVER + " = ?", new String[]{id, id}, null, null, TIMESTAMP);
        while(c.moveToNext()) {
            String receiver = c.getString(c.getColumnIndex(RECEIVER));
            String content = c.getString(c.getColumnIndex(CONTENT));
            Date timestamp = new Date(c.getLong(c.getColumnIndex(TIMESTAMP)));
            messages.add(new Message<ChatContent>(timestamp, id, receiver, new ChatContent(content)));
        }

        return messages;
    }

}
