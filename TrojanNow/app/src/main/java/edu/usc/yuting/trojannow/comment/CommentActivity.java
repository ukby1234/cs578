package edu.usc.yuting.trojannow.comment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.usc.yuting.trojannow.Intents;
import edu.usc.yuting.trojannow.R;
import edu.usc.yuting.trojannow.SendIntent;
import edu.usc.yuting.trojannow.login.User;
import edu.usc.yuting.trojannow.status.DashboardActivity;
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
