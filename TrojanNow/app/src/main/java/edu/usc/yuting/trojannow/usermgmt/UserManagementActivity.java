package edu.usc.yuting.trojannow.usermgmt;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

import edu.usc.yuting.trojannow.R;

public class UserManagementActivity extends ActionBarActivity {
    List<String> userIds = new LinkedList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddFriend(View v) {
        /*
        Add the selected userid into the list and cache database
        Contact the remote server to reflect changes
         */
    }

    public void onDeleteFriend(View v) {
        /*
        Delete the selected friend from the list and cache database
        Contact the remote server to reflect changes
         */
    }

    public void onRefreshFriend(View v) {
        /*
        Update the local database if necessary
        Clear the list cache and load from the database
         */
    }
}
