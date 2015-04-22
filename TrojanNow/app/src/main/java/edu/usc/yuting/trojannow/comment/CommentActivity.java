package edu.usc.yuting.trojannow.comment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import edu.usc.yuting.trojannow.common.Intents;
import edu.usc.yuting.trojannow.R;
import edu.usc.yuting.trojannow.common.SendIntent;
import edu.usc.yuting.trojannow.status.StatusActivity;

public class CommentActivity extends ActionBarActivity implements SendIntent {
    String sid = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Intent intent = getIntent();
        sid = intent.getStringExtra(Intents.intents.get("STATUS_INTENT_ID"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onUploadComment(View v) {
        /*
        Provide callbacks when users click on create comment
        It will construct an actual comment object, add to the local cache, and then send to the server
         */
        EditText text = (EditText)findViewById(R.id.createCommentEditText);
        CommentRepository.getInstance().createComment(sid,text.getText().toString(), this);

    }

    public void onCancelComment(View v) {
        /*
        First it will check whether the owner equals to the current users
        Delete the comment from the local cache and the remote server
         */
        onSendIntent();
    }

    @Override
    public void onSendIntent() {
        Intent intent = new Intent(this, StatusActivity.class);
        intent.putExtra(Intents.intents.get("STATUS_INTENT_ID"), sid);
        startActivity(intent);
    }
}
