package com.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Maintains a database
 * For more references: <a href>http://www.vogella.com/articles/AndroidSQLite/article.html#sqliteoverview_packages</a>
 */
public class MySQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "contacts.db";
    private static final int DATABASE_VERSION = 1;

    public MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlScript;
        try {
            sqlScript = new Scanner(new File("contacts.sql")).useDelimiter("\\A").next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        String[] sqlQueries = sqlScript.split(";");
        for (String query : sqlQueries) {
            db.execSQL(query);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to " +
                        newVersion + ", which will destroy all old data");
        onCreate(db);
    }

}
