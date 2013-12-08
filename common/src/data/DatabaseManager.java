package data;

import data.contents.ChatContent;

import java.security.PublicKey;
import java.util.ArrayList;

/**
 * @author Gary Ye
 * @version 12/2/13
 */
public interface DatabaseManager {
    /**
     * Calls the given create script. The create script is only allowed to have DDL statements like
     * CREATE, DROP, ALTER.
     *
     * @param createScriptFile the filename of the create script
     */
    public void createTables(String createScriptFile);

    /**
     * Return the user from the given id
     *
     * @return null if the user was not found or otherwise the data of the user
     */
    public User getUserFromGivenId(String id);

    /**
     * Returns the public of the given user
     *
     * @param id the given id
     * @return the public key of the given user
     */
    public PublicKey getPublicKeyFromId(String id);

    /**
     * Returns whether the user exists or not.
     *
     * @param id the given user id
     * @return whether the user exists or not
     */
    public boolean userExists(String id);

    /**
     * This method will insert the given user to the database.
     * Every attribute of the user will be inserted into.
     *
     * @param user the user
     */
    public void insertUser(User user);

    /**
     * Returns a list of all users in the lexicographical order from the database.
     *
     * @return all users in the lexicographical order as a list,
     */
    public ArrayList<User> loadUsers();

    /**
     * Remove the user with the given id from the database
     *
     * @param id the id of the user to remove
     * @return if the operation was successful
     */
    public boolean removeUser(String id);

    public void insertMessage(Message<ChatContent> chatContentMessage);

    public ArrayList<Message<ChatContent>> loadMessagesFromReceiver(String id);

    public ArrayList<Message<ChatContent>> loadMessagesFromSender(String id);

}
