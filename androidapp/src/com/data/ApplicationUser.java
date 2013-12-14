package com.data;

import android.app.Activity;
import com.security.AndroidKeyPairManager;
import crypto.Cryptography;
import crypto.Envelope;
import data.User;
import data.contents.ChatContent;
import data.contents.PublicKeyResponse;
import networking.SChatClient;
import networking.SChatServer;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.KeyPair;

/**
 * @author Gary Ye
 * @version 12/2/13
 *          One has to specify the following data:
 *          - host name (default: server name)
 *          - port ( default: port number )
 */
public class ApplicationUser extends User {
    private static ApplicationUser instance = null;

    private SChatClient client;
    private AndroidSQLManager dbMangager;

    private final static String hostName = "62.178.242.13";
    // private final static String hostName = "62.178.242.13";
    private final static int portNumber = SChatServer.PORT_ADDRESS;

    private ApplicationUser() throws IOException {
        this(hostName, portNumber);
    }

    private ApplicationUser(String hostName, int portNumber) throws IOException {
        dbMangager = new AndroidSQLManager();
        dbMangager.connect();

        // pass this object as a reference so the chat listener is able to call the method 'receiveMessage'
        // client = new SChatClient(this, hostName, portNumber, dbMangager);
    }

    public static ApplicationUser getInstance() throws IOException {
        if (instance == null) {
            synchronized (ApplicationUser.class) {
                if (instance == null) {
                    instance = new ApplicationUser();
                }
            }
        }
        return instance;
    }

    public void connect() {
        try {
            client = new SChatClient(this, hostName, portNumber, dbMangager);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public boolean registerToServer() {
        try {
            client.registerToServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean loginToServer() {
        try {
            client.loginToServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void sendMessage(ChatContent content, String receiver_id) {
        client.sendMessage(content, receiver_id);
    }

    @Override
    public void registerUser(Envelope envelope) {
        SecretKey secretKey1 = envelope.getUnwrappedKey(keyPair.getPrivate());
        PublicKeyResponse publicKeyResponse = envelope.<PublicKeyResponse>decryptMessage(secretKey1).getContent();
        dbMangager.insertUser(new User(publicKeyResponse.getRequestId(),
                new KeyPair(publicKeyResponse.getPublicKey(), null), null));
    }

    public void initialize(Activity activity) {
        if (!dbMangager.userExists(SChatServer.SERVER_ID)) {
            User server = new User(SChatServer.SERVER_ID, new KeyPair(AndroidKeyPairManager.getServerPK(activity), null), Cryptography.gen_symm_key());
            dbMangager.insertUser(server);
        }

        if (!AndroidKeyPairManager.isKeyPairAlreadySaved(activity)) {
            KeyPair keyPair1 = Cryptography.gen_asymm_key();
            AndroidKeyPairManager.saveKeyPair(activity, keyPair1);
        }

        keyPair = AndroidKeyPairManager.getKeyPairFormSharedPref(activity);
        id = "Gary";
    }
}
