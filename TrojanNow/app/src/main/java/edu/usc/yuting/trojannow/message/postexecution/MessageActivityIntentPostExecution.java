package edu.usc.yuting.trojannow.message.postexecution;

import android.app.Activity;
import android.content.Intent;

import edu.usc.yuting.trojannow.common.PostExecution;
import edu.usc.yuting.trojannow.message.activity.MessageActivity;

/**
 * Created by chengyey on 4/30/15.
 */

public class MessageActivityIntentPostExecution extends PostExecution{
    public MessageActivityIntentPostExecution(Activity activity) {
        super(activity);
    }

    @Override
    public void onPostExecution() {
        Intent intent = new Intent(activity, MessageActivity.class);
        activity.startActivity(intent);
    }
}
