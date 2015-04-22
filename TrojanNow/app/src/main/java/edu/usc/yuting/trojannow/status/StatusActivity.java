package edu.usc.yuting.trojannow.status;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.usc.yuting.trojannow.common.Intents;
import edu.usc.yuting.trojannow.R;
import edu.usc.yuting.trojannow.common.SendIntent;
import edu.usc.yuting.trojannow.common.UpdateUI;
import edu.usc.yuting.trojannow.comment.Comment;
import edu.usc.yuting.trojannow.comment.CommentActivity;
import edu.usc.yuting.trojannow.comment.CommentRepository;
import edu.usc.yuting.trojannow.login.User;
import edu.usc.yuting.trojannow.sensor.Sensor;
import edu.usc.yuting.trojannow.sensor.SensorRepository;

public class StatusActivity extends ActionBarActivity implements UpdateUI, SendIntent{
    Status status;
    private void redrawEnvironmentAttributes() {
        LinearLayout attributeLayout = (LinearLayout)findViewById(R.id.statusAttributeLayout);
        attributeLayout.removeAllViewsInLayout();
        for (Sensor sensor : SensorRepository.getInstance().getSensorsFromStatusId(status.getSid())) {
            try {
                LinearLayout innerLayout = new LinearLayout(this);
                innerLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                TextView information = new TextView(this);
                information.setText("\t\t" + sensor.getInformation());
                TextView source = new TextView(this);
                source.setText(sensor.getSource() + ": ");
                innerLayout.addView(source);
                innerLayout.addView(information);
                source.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                information.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                innerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                attributeLayout.addView(innerLayout);
            }catch(Exception e) {

            }
        }
    }

    private void redrawComments() {
        LinearLayout commentLayout = (LinearLayout)findViewById(R.id.statusCommentLayout);
        commentLayout.removeAllViewsInLayout();
        for (final Comment comment : CommentRepository.getInstance().getCommentsFromStatusId(status.getSid())) {
            try {
                LinearLayout innerLayout = new LinearLayout(this);
                TextView text = new TextView(this);
                text.setText("\t\t" + comment.getText());
                TextView username = new TextView(this);
                username.setText(comment.getUserName() + ": ");
                innerLayout.addView(username);
                innerLayout.addView(text);
                if (comment.getUserId().equals(User.getInstance().getUid())) {
                    Button delete = new Button(this);
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onDeleteComment(comment.getId());
                        }
                    });
                    delete.setText("Delete");
                    innerLayout.addView(delete);
                }
                username.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                innerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                commentLayout.addView(innerLayout);
            }catch(Exception e) {

            }
        }
    }
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
        SensorRepository.getInstance().refreshSensors(status.getSid(), this);
        CommentRepository.getInstance().refreshComments(status.getSid(),this);
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
        StatusRepository.getInstance().deleteStatus(status.getSid(), this);
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

    public void onUpdateStatus(View v) {
        /*
        Find the corresponding status
        Change the text inside the status
        Update both locally and remotely
         */
    }

    public void onDeleteComment(String cid) {
        /*
        Find the corresponding status
        Change the text inside the status
        Update both locally and remotely
         */
        CommentRepository.getInstance().deleteComment(cid, this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onSendIntent();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onUpdateUI() {
        redrawEnvironmentAttributes();
        redrawComments();
    }

    @Override
    public void onSendIntent() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }
}
