package edu.usc.yuting.trojannow.status.abstraction;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by chengyey on 3/30/15.
 */
public class Status implements Serializable, Comparable<Status>{
    private String username;
    private String uid;
    private String text;
    private String sid;
    private boolean isAnonymous;
    private Date timestamp;
    public Status(String sid, String username, String uid, String text, boolean isAnonymous, String timestamp) {
        this.username = username;
        this.text = text;
        this.sid = sid;
        this.isAnonymous = isAnonymous;
        this.uid = uid;
        try {
            this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(timestamp);
        }catch (Exception e) {
            this.timestamp = new Date();
        }
    }

    public Status(String uid, String text, boolean isAnonymous) {
        this.text = text;
        this.isAnonymous = isAnonymous;
        this.uid = uid;
        this.sid = UUID.randomUUID().toString() + super.hashCode();
    }

    @Override
    public int hashCode() {
        return sid.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Status) {
            Status status = (Status)o;
            return status.sid.equals(sid);
        }
        return false;
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

    public void setText(String text) {
        this.text = text;
    }

    public void setAnonymous(boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public Date getDate() {
        return timestamp;
    }

    @Override
    public int compareTo(Status another) {
        return -timestamp.compareTo(another.timestamp);
    }
}
