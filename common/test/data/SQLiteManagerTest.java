package data;

import crypto.Cryptography;
import crypto.Envelope;
import data.contents.ChatContent;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * @author Gary Ye
 * @version 12/1/13
 */
public class SQLiteManagerTest {
    SQLiteManager sqLiteManager;
    private final static String db = "junittest.db";

    @Before
    public void before() {
        sqLiteManager = new SQLiteManager(db);
        sqLiteManager.createTables("serverdb.sql");
    }

    @Test
    public void testGetPublicKeyFromId() throws Exception {
        String username = "user";
        byte[] seed = new byte[2];
        seed[0] = 101;
        seed[1] = 102;
        User user = new User(username, Cryptography.gen_asymm_key(seed), Cryptography.gen_symm_key(seed));
        sqLiteManager.insertUser(user);
        PublicKey publicKey = sqLiteManager.getPublicKeyFromId(username);
        assert (publicKey.equals(user.getKeyPair().getPublic()));
    }

    @Test
    public void testInsertUser() throws Exception {
        String username = "user";
        byte[] seed = new byte[2];
        seed[0] = 101;
        seed[1] = 102;
        sqLiteManager.insertUser(new User(username, Cryptography.gen_asymm_key(seed), Cryptography.gen_symm_key(seed)));
    }

    @Test
    public void testLoadUsers() throws Exception {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("Alice", Cryptography.gen_asymm_key(), Cryptography.gen_symm_key()));
        users.add(new User("Bob", Cryptography.gen_asymm_key(), Cryptography.gen_symm_key()));
        users.add(new User("Caesar", Cryptography.gen_asymm_key(), Cryptography.gen_symm_key()));
        for (User user : users)
            sqLiteManager.insertUser(user);
        ArrayList<User> lusers = sqLiteManager.loadUsers();
        assert (users.equals(lusers));
    }

    @Test
    public void testInsertEncryptedMessage() throws Exception {
        String text = "This is a simple Test!";

        String sender = "Alice";
        String receiver = "Bob";

        SecretKey skey = Cryptography.gen_symm_key();
        KeyPair keypair1 = Cryptography.gen_asymm_key();
        KeyPair keypair2 = Cryptography.gen_asymm_key();

        Message<ChatContent> message = new Message<>(Calendar.getInstance().getTime(), sender, receiver, new ChatContent(text));
        Envelope secure_message = new Envelope(message, skey, keypair2.getPublic(), keypair1.getPrivate());

        sqLiteManager.insertEncryptedMessage(secure_message);
        secure_message = sqLiteManager.loadEncryptedMessagesFromReceiver(receiver).get(0);

        SecretKey sharedSecretKey = secure_message.getUnwrappedKey(keypair2.getPrivate());
        String decryptedText = secure_message.<ChatContent>decryptMessage(sharedSecretKey, keypair1.getPublic()).getContent().toString();

        assert (text.equals(decryptedText));
    }


}
