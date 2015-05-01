package edu.usc.yuting.trojannow.usermgmt.abstraction;

/**
 * Created by chengyey on 4/30/15.
 */
public class FriendCandidate {
    private String uid;
    private String userName;
    public FriendCandidate(String uid, String userName) {
        this.uid = uid;
        this.userName = userName;
    }

    public String getUid() {
        return uid;
    }

    public String getUserName() {
        return userName;
    }
}
