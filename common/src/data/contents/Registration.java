package data.contents;

import data.Content;

import java.security.PublicKey;

/**
 * @author Gary Ye
 * @version 12/1/13
 */
public class Registration extends Content {
    private Login login;
    private PublicKey publicKey;

    /**
     * Constructs a registration content object.
     * @param login the login data
     * @param publicKey the public key of the user
     */
    public Registration(Login login, PublicKey publicKey) {
        this.login = login;
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        return login.toString();
    }

    public Login getLogin(){
        return login;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
