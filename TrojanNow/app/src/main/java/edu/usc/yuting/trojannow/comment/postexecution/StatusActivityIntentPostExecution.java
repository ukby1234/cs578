package edu.usc.yuting.trojannow.comment.postexecution;

import android.app.Activity;
import android.content.Intent;

import edu.usc.yuting.trojannow.common.Intents;
import edu.usc.yuting.trojannow.common.PostExecution;
import edu.usc.yuting.trojannow.status.activity.StatusActivity;

/**
 * Created by chengyey on 4/30/15.
 */

public class StatusActivityIntentPostExecution extends PostExecution{
    private String sid;
    public StatusActivityIntentPostExecution(Activity activity,String sid) {
        super(activity);
        this.sid = sid;
    }

    @Override
    public void onPostExecution() {
        Intent intent = new Intent(activity, StatusActivity.class);
        intent.putExtra(Intents.intents.get("STATUS_INTENT_ID"), sid);
        activity.startActivity(intent);
    }
}
