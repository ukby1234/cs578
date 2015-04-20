package edu.usc.yuting.trojannow.status;

import java.io.Serializable;

/**
 * Created by chengyey on 3/30/15.
 */
public class Status implements Serializable{
    private String username;
    private String uid;
    private String text;
    private String sid;
    private boolean isAnonymous;
    public Status(String sid, String username, String uid, String text, boolean isAnonymous) {
        this.username = username;
        this.text = text;
        this.sid = sid;
        this.isAnonymous = isAnonymous;
        this.uid = uid;
    }

    public String getUsername() {
        if (isAnonymous) {
            return "Anonymous";
        }
        return username;
    }

    public String getText() {
        return text;
    }

    public String getSid() {
        return sid;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public String getUid() {
        return uid;
    }
}
