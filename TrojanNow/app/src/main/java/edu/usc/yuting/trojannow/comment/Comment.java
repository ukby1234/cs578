package edu.usc.yuting.trojannow.comment;

/**
 * Created by chengyey on 3/30/15.
 */

import java.io.Serializable;
public class Comment implements Serializable{
    String cid;
    String userName;
    String text;
    public Comment(String cid, String userName, String text) {
        /*
        Set the owner of the comment, parent status, and comment text
         */
        this.cid = cid;
        this.userName = userName;
        this.text = text;
    }

    public String getCid() {
        return cid;
    }

    public String getUserName() {
        return userName;
    }

    public String getText() {
        return text;
    }
}
