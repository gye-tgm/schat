package data.contents;

import data.Content;

/**
 * @author Gary Ye
 * @version 11/25/13
 */
public class Login extends Content {
    private String id;

    /**
     * Create a Login content with the given id
     * @param id the id of the user to identify
     */
    public Login(String id) {
        this.type = Type.LOGIN;
        this.id = id;
    }

    /**
     * Return the id
     * @return the id
     */
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return id;
    }
}
