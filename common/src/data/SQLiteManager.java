package data;

import crypto.CryptoConstants;
import crypto.Cryptography;

import java.io.Writer;
import java.security.PublicKey;
import java.sql.*;
import java.util.ArrayList;

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

    public PublicKey getPublicKeyFromId(String receiverId) {
        String query = "SELECT public_key FROM user";
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        System.out.println(CryptoConstants.asymm_keylength / 8);
        byte[] publicKeyBytes = new byte[CryptoConstants.asymm_keylength / 8];

        System.out.println(publicKeyBytes.length);

        for(int i = 0; i < publicKeyBytes.length; i++)
            publicKeyBytes[i] = (byte)0;

        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                publicKeyBytes = rs.getBlob("public_key").getBytes(0, CryptoConstants.asymm_keylength / 8);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException ignored) {
            }
        }

        return Cryptography.getPublicKeyFromBytes(publicKeyBytes);
    }

    public void insertUser(User user){
        String sql = "INSERT INTO user VALUES(?,?,?) ";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql);
            ){
            Blob publicKeyBlob, symmetricKeyBlob;
            pstmt.setString(1, user.getId());

            publicKeyBlob = connection.createBlob();
            publicKeyBlob.setBytes(0, user.getKeyPair().getPublic().getEncoded());
            pstmt.setBlob(2, publicKeyBlob);

            symmetricKeyBlob = connection.createBlob();
            symmetricKeyBlob.setBytes(0, user.getSecretKey().getEncoded());
            pstmt.setBlob(3, symmetricKeyBlob);

            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Unexpected exception: " + ex.toString());
        }
    }

    /**
     * Load the names of all users
     * @return the name of all users
     */
    public ArrayList<String> loadUsers(){
        return null;
    }
}
