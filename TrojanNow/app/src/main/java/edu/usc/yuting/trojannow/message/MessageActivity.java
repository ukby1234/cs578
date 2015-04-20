package edu.usc.yuting.trojannow.message;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

import edu.usc.yuting.trojannow.R;

public class MessageActivity extends ActionBarActivity {
    List<Message> messages = new LinkedList<Message>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message, menu);
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

    public void onSendMessage(View v) {
        /*
        First construct the local object for a message
        Then add to the activity class and the local cache database
        Finally, make the remote call to the server
         */
    }

    public void onDeleteMessage(View v) {
        /*
        First find the corresponding message and delete both from local cache and list
        Make the remote call to the server, deleting the message
         */
    }

    public void onRefreshMessage(View v) {
        /*
        Update the local cache database if there is an Internet connection
        Clear the list and load everything from the cache database
         */
    }
}
