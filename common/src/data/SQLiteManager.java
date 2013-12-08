package data;

import crypto.Cryptography;
import crypto.Envelope;
import data.contents.ChatContent;

import javax.crypto.SecretKey;
import java.io.*;
import java.security.KeyPair;
import java.security.PublicKey;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * @author Gary Ye
 * @version 11/30/13
 *          This class can interact with an SQLite database and it offers
 *          the needed methods to retrieve or update data.
 */
public class SQLiteManager implements DatabaseManager {
    private String url; // the connection url, needed to specify the database to connect to.

    /**
     * Constructs a SQLite Manager, which can do update or queries
     * on the given database. The SQLiteManager needs the name of the
     * database to work on. If the name of the database is "client.db"
     * then one should pass this string to the constructor.
     *
     * @param db the database name
     */
    public SQLiteManager(String db) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.url = "jdbc:sqlite:" + db;
    }

    /**
     * Calls the given create script. The create script is only allowed to have DDL statements like
     * CREATE, DROP, ALTER.
     *
     * @param createScriptFile the filename of the create script
     */
    public void createTables(String createScriptFile) {
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();) {
            String sqlScript = new Scanner(new File(createScriptFile)).useDelimiter("\\A").next();
            String[] sqlQueries = sqlScript.split(";");
            for (String query : sqlQueries) {
                statement.executeUpdate(query);
            }
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the user from the given id
     *
     * @return null if the user was not found or otherwise the data of the user
     */
    public User getUserFromGivenId(String id) {
        String query = "SELECT id, public_key, symmetric_key FROM user WHERE id = \'" + id + "\'";
        PublicKey publicKey = null;
        SecretKey secretKey = null;
        try (
                Connection connection = DriverManager.getConnection(url);
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query)
        ) {
            if (!rs.next())
                return null;
            publicKey = Cryptography.getPublicKeyFromBytes(rs.getBytes("public_key"));
            secretKey = Cryptography.getSecretKeyFromBytes(rs.getBytes("symmetric_key")); // sprint(publicKeyBytes);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return new User(id, new KeyPair(publicKey, null), secretKey);
    }

    /**
     * Returns the public of the given user
     *
     * @param id the given id
     * @return the public key of the given user
     */
    public PublicKey getPublicKeyFromId(String id) {
        return getUserFromGivenId(id).getKeyPair().getPublic();
    }

    /**
     * Returns whether the user exists or not.
     *
     * @param id the given user id
     * @return whether the user exists or not
     */
    public boolean userExists(String id) {
        return getUserFromGivenId(id) != null;
    }

    /**
     * Execute a simple DDL query like INSERT, UPDATE or ALTER.
     *
     * @param sql the sql query
     */
    public void executeQuery(String sql) {
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
        ) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method will insert the given user to the database.
     * Every attribute of the user will be inserted into.
     *
     * @param user the user
     */
    public void insertUser(User user) {
        String sql = "INSERT INTO user VALUES(?,?,?) ";
        try (Connection connection = getConnection();
        ) {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, user.getId());
            // In SQLite you set blob as bytes
            if (user.getPublicKey() != null)
                pstmt.setBytes(2, user.getKeyPair().getPublic().getEncoded());
            if (user.getSecretKey() != null)
                pstmt.setBytes(3, user.getSecretKey().getEncoded());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Unexpected exception: " + ex.toString());
        }
    }

    /**
     * Returns a list of all users in the lexicographical order from the database.
     *
     * @return all users in the lexicographical order as a list,
     */
    public ArrayList<User> loadUsers() {
        String sql = "SELECT id, public_key, symmetric_key FROM user ORDER BY id ASC";
        ArrayList<User> list = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
        ) {
            while (rs.next()) {
                String id = rs.getString("id");
                PublicKey publicKey = Cryptography.getPublicKeyFromBytes(rs.getBytes("public_key"));
                SecretKey secretKey = Cryptography.getSecretKeyFromBytes(rs.getBytes("symmetric_key"));
                list.add(new User(id, new KeyPair(publicKey, null), secretKey));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Get a connection from the driver manager
     *
     * @return a connection
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

    /**
     * Remove the user with the given id from the database
     *
     * @param id the id of the user to remove
     * @return if the operation was successful
     */
    public boolean removeUser(String id) {
        executeQuery(String.format("DELETE FROM user WHERE id = '%s'", id)); // TODO: check if an SQL injection can occur?!
        return true;
    }


    public void insertEncryptedMessage(Envelope envelope) {
        try (
                Connection connection = getConnection();
                PreparedStatement pstmt = connection.prepareStatement("INSERT INTO message VALUES(?,?,?)");
        ) {
            pstmt.setString(1, envelope.getReceiver());
            pstmt.setLong(2, Calendar.getInstance().getTimeInMillis());
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 ObjectOutput out = new ObjectOutputStream(bos)
            ) {
                out.writeObject(envelope);
                pstmt.setBytes(3, bos.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Envelope> loadEncryptedMessagesFromReceiver(String id) {
        ArrayList<Envelope> ret = new ArrayList<>();
        try (
                Connection connection = getConnection();
                PreparedStatement pstmt = connection.prepareStatement("SELECT content" +
                        " FROM message WHERE receiver_id = ? ORDER BY timestamp ASC")
        ) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                try (
                        ByteArrayInputStream bis = new ByteArrayInputStream(rs.getBytes(1));
                        ObjectInput in = new ObjectInputStream(bis)
                ) {
                    ret.add((Envelope) in.readObject());
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }


    @Override
    public void insertMessage(Message<ChatContent> message) {
        try (
                Connection connection = getConnection();
                PreparedStatement pstmt = connection.prepareStatement("INSERT INTO message VALUES(?,?,?,?)");
        ) {
            pstmt.setString(1, message.getSender());
            pstmt.setString(2, message.getReceiver());
            pstmt.setLong(3, message.getTimestamp().getTime() / 1000);
            pstmt.setString(4, message.getContent().getMessage());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Message<ChatContent>> loadMessagesFromReceiver(String id) {
        return loadMessagesFromUser("receiver_id", id);
    }

    public ArrayList<Message<ChatContent>> loadMessagesFromSender(String id) {
        return loadMessagesFromUser("sender_id", id);
    }


    public ArrayList<Message<ChatContent>> loadMessagesFromUser(String who, String id) {
        ArrayList<Message<ChatContent>> ret = new ArrayList<>();

        try (
                Connection connection = getConnection();
                PreparedStatement pstmt = connection.prepareStatement("SELECT sender_id, receiver_id, timestamp, content" +
                        " FROM message WHERE " + who + " = ? ORDER BY timestamp ASC")
        ) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String senderId = rs.getString(1);
                String receiverId = rs.getString(2);
                long timestamp = rs.getLong(3);
                String content = rs.getString(4);
                ret.add(new Message<ChatContent>(new Date(timestamp), senderId, receiverId, new ChatContent(content)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
