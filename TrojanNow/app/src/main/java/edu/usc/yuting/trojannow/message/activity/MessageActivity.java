package edu.usc.yuting.trojannow.message.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

import edu.usc.yuting.trojannow.R;
import edu.usc.yuting.trojannow.database.MessageRepository;
import edu.usc.yuting.trojannow.login.abstraction.User;
import edu.usc.yuting.trojannow.login.activity.LoginActivity;
import edu.usc.yuting.trojannow.message.abstraction.Message;
import edu.usc.yuting.trojannow.message.postexecution.DrawMessagesPostExecution;
import edu.usc.yuting.trojannow.status.activity.DashboardActivity;
import edu.usc.yuting.trojannow.usermgmt.activity.UserManagementActivity;

public class MessageActivity extends ActionBarActivity{
    List<Message> messages = new LinkedList<Message>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        MessageRepository.getInstance().refreshMessage(new DrawMessagesPostExecution(this));
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
        if (id == R.id.action_dashboard) {
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_logout) {
            User.getInstance().logout();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_user_mgmt) {
            Intent intent = new Intent(this, UserManagementActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onSendMessage(View v) {
        /*
        First construct the local object for a message
        Then add to the activity class and the local cache database
        Finally, make the remote call to the server
         */
        Intent intent = new Intent(this, CreateMessageActivity.class);
        startActivity(intent);
    }

    public void onRefreshMessage(View v) {
        /*
        Update the local cache database if there is an Internet connection
        Clear the list and load everything from the cache database
         */
        MessageRepository.getInstance().refreshMessage(new DrawMessagesPostExecution(this));
    }

}
