package edu.usc.yuting.trojannow.comment;

/**
 * Created by chengyey on 3/30/15.
 */

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Comment implements Serializable, Comparable<Comment>{
    private String id;
    private String userName;
    private String text;
    private String statusId;
    private String userId;
    private Date timestamp = new Date();
    public Comment(String id, String statusId, String userId, String userName, String text, String timestamp) {
        /*
        Set the owner of the comment, parent status, and comment text
         */
        this.id = id;
        this.statusId = statusId;
        this.userName = userName;
        this.text = text;
        this.userId = userId;
        try {
            this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(timestamp);
        }catch (Exception e) {

        }

    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getText() {
        return text;
    }

    public String getStatusId() {
        return statusId;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public int compareTo(Comment another) {
        return -timestamp.compareTo(another.timestamp);
    }
}
