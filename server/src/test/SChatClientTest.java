package test;

import crypto.Cryptography;
import data.KeyPairManager;
import data.SQLiteManager;
import data.User;
import data.contents.ChatContent;
import networking.SChatClient;
import networking.SChatServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Tests the SChatClient implementation.
 *
 * @author Gary Ye
 */
public class SChatClientTest {
    public static User getMyUser(String username) {
        byte[] seed = new byte[2];
        seed[0] = 101;
        seed[1] = 102;
        return new User(username, Cryptography.gen_asymm_key(seed), Cryptography.gen_symm_key(seed));
    }

    public static User getServerUser() {
        return new User(SChatServer.SERVER_ID, KeyPairManager.readKeyPair("public.key", "private.key"),
                Cryptography.gen_symm_key());
    }

    public static void init(User user, String dbname) {
        SQLiteManager sqLiteManager = new SQLiteManager(dbname);
        sqLiteManager.createTables("clientdb.sql");
        sqLiteManager.insertUser(user);
        sqLiteManager.insertUser(getServerUser());
    }
    public static SChatClient initalize(String[] args){
        String username = null, hostName = null, dbFile = null;
        int portNumber = 0;

        try{
            username = args[0];
            hostName = args[1];
            portNumber = Integer.parseInt(args[2]);
        }catch (Exception e){
            System.err.println("usage: java test.SChatClientTest <id> <host name> <port> <db name>");
            System.exit(1);
        }

        User me = getMyUser(username);
        init(me, dbFile);

        SChatClient client = null;
        try {
            client = new SChatClient(hostName, portNumber, me);
            client.registerToServer();
        } catch (IOException e) {
            System.err.println("Could not connect to the server successfully.");
            e.printStackTrace();
            System.exit(1);
        }
        return client;
    }
    public static void main(String[] args) throws IOException {
        SChatClient client = initalize(args);
        /**
         * 1) add contact
         * 2) remove contact
         * 3) send message
         *      choose contact from list
         *      send it
         */
        System.out.println("Logged in!");
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while ((line = in.readLine()) != null) {
                String[] messageSplit = line.split(" ");
                if (messageSplit.length >= 2) {
                    String receiverId = messageSplit[0];
                    StringBuilder message = new StringBuilder("");
                    for (int i = 1; i < messageSplit.length; i++) {
                        message.append(messageSplit[i]);
                        message.append(" \n".charAt(i + 1 == messageSplit.length ? 1 : 0));
                    }

                    client.sendMessage(new ChatContent(message.toString()), receiverId);
                } else {
                    System.err.println("usage: <receiver id> <message>");
                }
            }
            in.close();
        } catch (IOException e) {
            System.err.println("IOException occurred : " + e.getMessage());
            System.exit(1);
        }
    }
}
