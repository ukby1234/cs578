package edu.usc.yuting.trojannow.login.postexecution;

import android.app.Activity;
import android.content.Intent;

import edu.usc.yuting.trojannow.common.PostExecution;
import edu.usc.yuting.trojannow.status.activity.DashboardActivity;

/**
 * Created by chengyey on 4/30/15.
 */
public class DashboardActivityIntentPostExecution extends PostExecution{
    public DashboardActivityIntentPostExecution(Activity activity) {
        super(activity);
    }

    @Override
    public void onPostExecution() {
        Intent intent = new Intent(activity, DashboardActivity.class);
        activity.startActivity(intent);
    }
}
