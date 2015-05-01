package edu.usc.yuting.trojannow.status.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import edu.usc.yuting.trojannow.R;
import edu.usc.yuting.trojannow.database.StatusRepository;
import edu.usc.yuting.trojannow.login.abstraction.User;
import edu.usc.yuting.trojannow.login.activity.LoginActivity;
import edu.usc.yuting.trojannow.message.activity.MessageActivity;
import edu.usc.yuting.trojannow.status.postexecution.DrawDashboardPostExecution;
import edu.usc.yuting.trojannow.usermgmt.activity.UserManagementActivity;

public class DashboardActivity extends ActionBarActivity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        StatusRepository.getInstance().refreshStatuses(new DrawDashboardPostExecution(this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_messages) {
            Intent intent = new Intent(this, MessageActivity.class);
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

    public void onRefreshStatus(View v) {
        /*
        Update the local cache database if necessary
        Clear the list cache, and reload from the cache database
         */
        StatusRepository.getInstance().refreshStatuses(new DrawDashboardPostExecution(this));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onCreateStatus(View v) {
        /*
        Create the actual status
        Create both locally and remotely
         */
        Intent intent = new Intent(this, CreateStatusActivity.class);
        startActivity(intent);
    }

}
