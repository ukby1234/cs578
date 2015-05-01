package edu.usc.yuting.trojannow.status.postexecution;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import edu.usc.yuting.trojannow.R;
import edu.usc.yuting.trojannow.common.Intents;
import edu.usc.yuting.trojannow.common.PostExecution;
import edu.usc.yuting.trojannow.database.StatusRepository;
import edu.usc.yuting.trojannow.status.abstraction.Status;
import edu.usc.yuting.trojannow.status.activity.StatusActivity;

/**
 * Created by chengyey on 4/30/15.
 */

public class DrawDashboardPostExecution extends PostExecution{
    public DrawDashboardPostExecution(Activity activity) {
        super(activity);
    }

    @Override
    public void onPostExecution() {
        LinearLayout linearLayout = (LinearLayout)activity.findViewById(R.id.dashboardLayout);
        linearLayout.removeAllViewsInLayout();
        for (final Status status : StatusRepository.getInstance().getAllStatuses()) {
            try {
                LinearLayout innerLayout = new LinearLayout(activity);
                innerLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDisplay(status);
                    }
                });
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                int padding = (int)activity.getResources().getDimension(R.dimen.table_padding);
                TextView text = new TextView(activity);
                text.setText(status.getText());
                TextView username = new TextView(activity);
                username.setText(status.getUsername() + ":");
                TextView date = new TextView(activity);
                date.setText(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(status.getDate()));
                date.setPadding(padding, padding, padding, padding);
                username.setPadding(padding, padding, padding, padding);
                text.setPadding(padding, padding, padding, padding);
                date.setPadding(padding, padding, padding, padding);
                innerLayout.addView(username);
                innerLayout.addView(text);
                innerLayout.addView(date);
                username.setLayoutParams(params);
                text.setLayoutParams(params);
                linearLayout.addView(innerLayout);
            }catch(Exception e) {

            }
        }
    }

    public void onDisplay( Status status) {
        Intent intent = new Intent(activity, StatusActivity.class);
        intent.putExtra(Intents.intents.get("STATUS_INTENT_ID"), status.getSid());
        activity.startActivity(intent);
    }
}
