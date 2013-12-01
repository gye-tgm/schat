package data.contents;

import data.Content;

/**
 * @author Gary Ye
 * @version 12/1/13
 */
public class PublicKeyRequest extends Content {
    private String requestId;
    public PublicKeyRequest(String requestId){
        this.type = Type.PUBLIC_KEY_REQUEST;
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "Public key request from " + getRequestId();
    }

    public String getRequestId() {
        return requestId;
    }
}
