package edu.usc.yuting.trojannow.comment;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

import edu.usc.yuting.trojannow.R;

public class CommentActivity extends ActionBarActivity {
    List<Comment> comments = new LinkedList<Comment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onCreateComment(View v) {
        /*
        Provide callbacks when users click on create comment
        It will construct an actual comment object, add to the local cache, and then send to the server
         */
    }

    public void onDeleteComment(View v) {
        /*
        First it will check whether the owner equals to the current users
        Delete the comment from the local cache and the remote server
         */
    }

    public void onRefreshComment(View v) {
        /*
        Contact the remote server to do certain updates
        Load to the local cache database
        Clear the list, and load everything from the cache database
         */
    }
}
