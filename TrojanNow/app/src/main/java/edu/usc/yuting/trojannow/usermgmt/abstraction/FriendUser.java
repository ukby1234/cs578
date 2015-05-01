package edu.usc.yuting.trojannow.usermgmt.abstraction;

/**
 * Created by chengyey on 4/30/15.
 */
public class FriendUser {
    private String uid;
    private String userName;
    private boolean isAccepted;
    private boolean direction;
    public FriendUser(String uid, String userName, boolean isAccepted, boolean direction) {
        this.uid = uid;
        this.userName = userName;
        this.isAccepted = isAccepted;
        this.direction = direction;
    }

    public String getUid() {
        return uid;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        this.isAccepted = accepted;
    }

    public boolean getDirection() {
        return direction;
    }
}
