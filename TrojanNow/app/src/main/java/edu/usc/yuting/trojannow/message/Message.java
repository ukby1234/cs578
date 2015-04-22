package edu.usc.yuting.trojannow.message;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by chengyey on 3/30/15.
 */
public class Message implements Comparable<Message>, Serializable{
    private String id;
    private String senderId;
    private String receiverId;
    private String text;
    private Date timestamp = new Date();
    private String senderName;
    private String receiverName;
    public Message(String id, String senderId, String senderName, String receiverId, String receiverName, String text, String timestamp) {
        /*
        The constructor for the wrapper class for message
        Set the instance variable
         */
        this.id = id;
        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.text = text;
        try {
            this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(timestamp);
        }catch (Exception e) {

        }
    }

    public Message(String senderId, String receiverId, String text) {
        this.senderId = senderId;
        this.receiverId  = receiverId;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getText() {
        return text;
    }

    @Override
    public int compareTo(Message another) {
        return -timestamp.compareTo(another.timestamp);
    }
}
