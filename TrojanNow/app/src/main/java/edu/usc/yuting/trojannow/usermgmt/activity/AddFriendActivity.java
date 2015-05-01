package edu.usc.yuting.trojannow.usermgmt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import edu.usc.yuting.trojannow.R;
import edu.usc.yuting.trojannow.database.FriendRepository;
import edu.usc.yuting.trojannow.usermgmt.postexecution.DrawAddFriendPostExecution;

public class AddFriendActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        FriendRepository.getInstance().refreshCandidates(new DrawAddFriendPostExecution(this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onCancelAddFriend(View v) {
        Intent intent = new Intent(this, UserManagementActivity.class);
        startActivity(intent);
    }
}
