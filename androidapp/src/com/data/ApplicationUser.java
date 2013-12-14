package com.data;

import android.content.Context;
import com.security.AndroidKeyPairManager;
import crypto.Cryptography;
import data.User;
import data.contents.ChatContent;
import networking.SChatClient;
import networking.SChatServer;

import java.io.IOException;
import java.security.KeyPair;

/**
 * @author Gary Ye
 * @version 12/2/13
 * One has to specify the following data:
 *  - host name (default: server name)
 *  - port ( default: port number )
 *
 */
public class ApplicationUser extends User {
    private static ApplicationUser instance = null;

    private SChatClient client;
    private AndroidSQLManager dbMangager;

    private final static String hostName = "85.10.240.108";
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
        if(instance == null){
            synchronized (ApplicationUser.class){
                if (instance == null) {
                    instance = new ApplicationUser();
                }
            }
        }
        return instance;
    }

    public boolean registerToServer(){
        try {
            client.registerToServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean loginToServer(){
        try{
            client.loginToServer();
        } catch(IOException e){
            e.printStackTrace();
        }
        return true;
    }

    public void initialize(Context context) {
        if(!dbMangager.userExists(SChatServer.SERVER_ID)) {
            User server = new User(SChatServer.SERVER_ID, new KeyPair(AndroidKeyPairManager.getServerPK(context), null), Cryptography.gen_symm_key());
            dbMangager.insertUser(server);
        }
    }
}
