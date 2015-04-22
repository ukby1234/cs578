package edu.usc.yuting.trojannow.status;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import edu.usc.yuting.trojannow.Intents;
import edu.usc.yuting.trojannow.R;
import edu.usc.yuting.trojannow.UpdateUI;
import edu.usc.yuting.trojannow.message.MessageActivity;

public class DashboardActivity extends ActionBarActivity implements UpdateUI{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        StatusRepository.getInstance().refreshStatuses(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_messaages) {
            Intent intent = new Intent(this, MessageActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void onDisplay( Status status) {
        Intent intent = new Intent(this, StatusActivity.class);
        intent.putExtra(Intents.intents.get("STATUS_INTENT_ID"), status.getSid());
        startActivity(intent);
    }

    public void onRefreshStatus(View v) {
        /*
        Update the local cache database if necessary
        Clear the list cache, and reload from the cache database
         */
        StatusRepository.getInstance().refreshStatuses(this);
    }

    public void onCreateStatus(View v) {
        /*
        Create the actual status
        Create both locally and remotely
         */
        Intent intent = new Intent(this, CreateStatusActivity.class);
        startActivity(intent);
    }

    @Override
    public void onUpdateUI() {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.dashboardLayout);
        linearLayout.removeAllViewsInLayout();
        for (final edu.usc.yuting.trojannow.status.Status status : StatusRepository.getInstance().getAllStatuses()) {
            try {
                LinearLayout innerLayout = new LinearLayout(this);
                innerLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDisplay(status);
                    }
                });
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextView text = new TextView(this);
                text.setText("\t\t" + status.getText());
                TextView username = new TextView(this);
                username.setText(status.getUsername() + ": ");
                innerLayout.addView(username);
                innerLayout.addView(text);
                username.setLayoutParams(params);
                text.setLayoutParams(params);
                linearLayout.addView(innerLayout);
            }catch(Exception e) {

            }
        }
    }
}
