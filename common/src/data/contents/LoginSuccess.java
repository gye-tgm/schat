package data.contents;

import data.Content;

/**
 * @author Gary Ye
 * @version 12/17/13
 */
public class LoginSuccess extends Content {
    boolean success;

    /**
     * Create a Login content with the given id
     */
    public LoginSuccess(boolean success) {
        this.type = Type.LOGIN_SUCCESS;
        this.success = success;
    }

    /**
     * Returns the content, properly formatted and readable in a String.
     *
     * @return the content in a String
     */
    @Override
    public String toString() {
        return success ? "Login success!" : "Login failed!";
    }
}
