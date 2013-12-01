package data.contents;

import data.Content;

import java.security.PublicKey;

/**
 * @author Gary Ye
 * @version 12/1/13
 */
public class PublicKeyResponse extends Content {

    private String requestId;
    private PublicKey publicKey;

    public PublicKeyResponse(String requestId, PublicKey publicKey) {
        this.type = Type.PUBLIC_KEY_RESPONSE;
        this.requestId = requestId;
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        return "Public key of " + requestId + " is inside";
    }

    public String getRequestId() {
        return requestId;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    boolean userNotExisting(){
        return publicKey == null;
    }
}
