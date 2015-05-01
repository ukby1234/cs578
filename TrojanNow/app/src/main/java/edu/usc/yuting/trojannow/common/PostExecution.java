package edu.usc.yuting.trojannow.common;

import android.app.Activity;

/**
 * Created by chengyey on 4/30/15.
 */
public abstract class PostExecution {
    protected Activity activity;
    public PostExecution(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }

    public abstract void onPostExecution();
}
