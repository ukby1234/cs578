package edu.usc.yuting.trojannow.status.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.usc.yuting.trojannow.R;
import edu.usc.yuting.trojannow.comment.activity.CommentActivity;
import edu.usc.yuting.trojannow.common.Intents;
import edu.usc.yuting.trojannow.database.CommentRepository;
import edu.usc.yuting.trojannow.database.StatusRepository;
import edu.usc.yuting.trojannow.login.abstraction.User;
import edu.usc.yuting.trojannow.login.postexecution.DashboardActivityIntentPostExecution;
import edu.usc.yuting.trojannow.database.SensorRepository;
import edu.usc.yuting.trojannow.status.abstraction.Status;
import edu.usc.yuting.trojannow.status.postexecution.DrawStatusActivityPostExecution;

public class StatusActivity extends ActionBarActivity{
    Status status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Intent intent = getIntent();
        String sid = intent.getStringExtra(Intents.intents.get("STATUS_INTENT_ID"));
        status = StatusRepository.getInstance().getStatus(sid);
        TextView userName = (TextView)findViewById(R.id.statusUserNameText);
        userName.setText(status.getUsername() + ": ");
        TextView content = (TextView)findViewById(R.id.statusContentText);
        content.setText("\t\t" + status.getText());
        Button button = (Button)findViewById(R.id.deleteStatusButton);
        if (User.getInstance().getUid().equals(status.getUid()) ) {
            button.setVisibility(Button.VISIBLE);
        }
        else {
            button.setVisibility(Button.GONE);
        }
        SensorRepository.getInstance().refreshSensors(status.getSid(), new DrawStatusActivityPostExecution(this, status));
        CommentRepository.getInstance().refreshComments(status.getSid(),new DrawStatusActivityPostExecution(this, status));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onDeleteStatus(View v) {
        /*
        Find the corresponding status
        Delete both locally and remotely
         */
        StatusRepository.getInstance().deleteStatus(status.getSid(), new DashboardActivityIntentPostExecution(this));
    }

    public void onCreateComment(View v) {
        /*
        Find the corresponding status
        Delete both locally and remotely
         */
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra(Intents.intents.get("STATUS_INTENT_ID"), status.getSid());
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            new DashboardActivityIntentPostExecution(this).onPostExecution();
        }
        return super.onKeyDown(keyCode, event);
    }

}
