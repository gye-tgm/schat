package com.security;

import android.R;
import android.content.Context;
import android.content.res.Resources;
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

    public static void saveKeyPair(Context context, KeyPair keyPair) {
    }
}
