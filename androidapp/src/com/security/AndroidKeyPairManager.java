package com.security;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import crypto.Cryptography;
import data.KeyPairManager;

import java.io.*;
import java.security.KeyPair;
import java.security.PublicKey;

/**
 *
 */
public class AndroidKeyPairManager {
    public static final int PK_FILE_LENGTH = 294;

    public static final String PUB_KEY = "pub_key";
    public static final String PRIVATE_KEY = "private_key";

    public static PublicKey getServerPK(Context context) {
        PublicKey pk = null;

        try {
            byte[] bytes = new byte[294];
            DataInputStream reader = new DataInputStream(context.getAssets().open("keys/public.key"));
            reader.readFully(bytes);
            reader.close();

            pk = Cryptography.getPublicKeyFromBytes(bytes);
        } catch (IOException e) {
            String s = e.getMessage();
            Log.d("?", s);
        }

        return pk;
    }

    public static void saveKeyPair(Activity activity, KeyPair keyPair) {
        SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = shre.edit();

        String privateKey = Base64.encodeToString(keyPair.getPrivate().getEncoded(), Base64.DEFAULT);
        String publicKey = Base64.encodeToString(keyPair.getPublic().getEncoded(), Base64.DEFAULT);

        editor.putString(PRIVATE_KEY, privateKey);
        editor.putString(PUB_KEY, publicKey);
        editor.commit();
    }

    public static KeyPair getKeyPairFormSharedPref(Activity activity) {
        SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(activity);
        byte[] privateKey = Base64.decode(shre.getString(PRIVATE_KEY, ""), Base64.DEFAULT);
        byte[] publicKey = Base64.decode(shre.getString(PUB_KEY, ""), Base64.DEFAULT);
        KeyPair keyPair = new KeyPair(Cryptography.getPublicKeyFromBytes(publicKey), Cryptography.getPrivateKeyFromBytes(privateKey));
        return keyPair;
    }

    public static boolean isKeyPairAlreadySaved(Activity activity) {
        SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(activity);
        return shre.contains(PUB_KEY) && shre.contains(PRIVATE_KEY);
    }
}
