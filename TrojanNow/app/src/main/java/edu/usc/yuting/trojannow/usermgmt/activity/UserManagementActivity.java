package edu.usc.yuting.trojannow.usermgmt.activity;

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
import edu.usc.yuting.trojannow.database.FriendRepository;
import edu.usc.yuting.trojannow.login.abstraction.User;
import edu.usc.yuting.trojannow.login.activity.LoginActivity;
import edu.usc.yuting.trojannow.message.activity.MessageActivity;
import edu.usc.yuting.trojannow.status.activity.DashboardActivity;
import edu.usc.yuting.trojannow.usermgmt.postexecution.DrawUserManagementPostExecution;

public class UserManagementActivity extends ActionBarActivity {
    List<String> userIds = new LinkedList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);
        FriendRepository.getInstance().updateFriendList(new DrawUserManagementPostExecution(this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_management, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

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

        if (id == R.id.action_messages) {
            Intent intent = new Intent(this, MessageActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddFriend(View v) {
        /*
        Add the selected userid into the list and cache database
        Contact the remote server to reflect changes
         */
        Intent intent = new Intent(this, AddFriendActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
