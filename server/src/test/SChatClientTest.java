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
    private static SQLiteManager sqLiteManager;
    private static User me;

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

    public static SChatClient initalize(String[] args) {
        String username = null, hostName = null, dbFile = null;
        int portNumber = 0;

        try {
            username = args[0];
            hostName = args[1];
            portNumber = Integer.parseInt(args[2]);
            dbFile = args[3];
        } catch (Exception e) {
            System.err.println("usage: java test.SChatClientTest <id> <host name> <port> <db name>");
            System.exit(1);
        }

        me = getMyUser(username);

        sqLiteManager = new SQLiteManager(dbFile);


        sqLiteManager.createTables("clientdb.sql");
        sqLiteManager.insertUser(me);
        sqLiteManager.insertUser(getServerUser());


        SChatClient client = null;
        try {
            client = new SChatClient(me, hostName, portNumber);
            client.registerToServer();
        } catch (IOException e) {
            System.err.println("Could not connect to the server successfully.");
            e.printStackTrace();
            System.exit(1);
        }
        return client;
    }

    public static void printOptions() {
        System.out.println("1) Add contact\n" +
                "2) Remove contact\n" +
                "3) Send message\n" +
                "4) Show contact list\n");
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
            printOptions();
            while ((line = in.readLine()) != null) {
                try {
                    String[] messageSplit = line.split(" ");
                    int option = Integer.parseInt(messageSplit[0]);
                    if (option == 1) {
                        String id = messageSplit[1];
                        // Retrieve from server
                        client.sendPublicKeyRequest(id);
                    } else if (option == 2) {
                        String id = messageSplit[1];
                        sqLiteManager.removeUser(id);
                    } else if (option == 3) {
                        String receiverId = messageSplit[1];
                        StringBuilder message = new StringBuilder("");
                        for (int i = 2; i < messageSplit.length; i++) {
                            message.append(messageSplit[i]);
                            message.append(" \n".charAt(i + 1 == messageSplit.length ? 1 : 0));
                        }
                        client.sendMessage(new ChatContent(message.toString()), receiverId);
                    } else if (option == 4) {
                        System.out.println("***Contact list***");
                        for (User user : sqLiteManager.loadUsers()) {
                            System.out.println(user.getName());
                        }
                        System.out.println("********************\n");
                    } else {
                        System.err.println("usage: <option> <option args>");
                    }
                } catch (Exception ex) {
                    System.err.println("usage: <option> <option args>");
                } finally {
                    printOptions();
                }
            }
            in.close();
        } catch (IOException e) {
            System.err.println("IOException occurred : " + e.getMessage());
            System.exit(1);
        }
    }
}
