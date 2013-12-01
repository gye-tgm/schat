package data;

import crypto.Cryptography;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.security.PublicKey;

/**
 * @author Gary Ye
 * @version 12/1/13
 */
public class SQLiteManagerTest {
    SQLiteManager sqLiteManager;
    private final static String db = "junittest.db";


    @BeforeClass
    public static void beforeClass() {
        SQLiteManager sqLiteManager = new SQLiteManager(db);
        sqLiteManager.createTables("clientdb.sql");
    }

    @Before
    public void before() {
        sqLiteManager = new SQLiteManager(db);
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
        assert (false); // Not implemented
    }
}
