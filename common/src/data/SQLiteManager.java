package data;

import crypto.CryptoConstants;
import crypto.Cryptography;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author Gary Ye
 * @version 11/30/13
 */
public class SQLiteManager {
    // suggestion: using the jdbc pooling method described here:
    // http://www.javaranch.com/journal/200601/JDBCConnectionPooling.html
    private String url;

    public SQLiteManager(String db) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.url = "jdbc:sqlite:" + db;
    }

    /**
     * Calls the create script
     */
    public void createTables(String createScriptFile) {
        try (
                Connection connection = DriverManager.getConnection(url);
                Statement statement = connection.createStatement();
        ){
            String sqlScript;
            try {
                sqlScript = new Scanner(new File(createScriptFile)).useDelimiter("\\A").next();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }

            String[] sqlQueries = sqlScript.split(";");
            for (String query : sqlQueries) {
                statement.executeUpdate(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the user from the given id
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
            if(!rs.next())
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
     * @param id the given id
     * @return the public key of the given user
     */
    public PublicKey getPublicKeyFromId(String id) {
        return getUserFromGivenId(id).getKeyPair().getPublic();
    }

    /**
     * Returns whether the user exists or not.
     * @param id the given user id
     * @return whether the user exists or not
     */
    public boolean userExists(String id){
        return getUserFromGivenId(id) != null;
    }
    /**
     * Execute a simple query
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
        try (Connection connection = DriverManager.getConnection(url);
        ) {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, user.getId());
            // In SQLite you set blob as bytes
            if(user.getPublicKey() != null)
                pstmt.setBytes(2, user.getKeyPair().getPublic().getEncoded());
            if(user.getSecretKey() != null)
                pstmt.setBytes(3, user.getSecretKey().getEncoded());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Unexpected exception: " + ex.toString());
        }
    }

    /**
     * Load all users from the database and
     * return a list of them in the lexicographical order.
     *
     * @return all users in the lexicographical order
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
     * @return a connection
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

    /**
     * Remove the user with the given id from the database
     * @param id the id of the user to remove
     * @return if the operation was successful
     */
    public boolean removeUser(String id) {
        executeQuery(String.format("DELETE FROM user WHERE id = '%s'",  id)); // TODO: check if there is SQL injection?!
        return true;
    }
}
