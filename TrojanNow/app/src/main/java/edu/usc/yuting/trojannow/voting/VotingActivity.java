package edu.usc.yuting.trojannow.voting;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

import edu.usc.yuting.trojannow.R;

public class VotingActivity extends ActionBarActivity {
    List<Vote> votes = new LinkedList<Vote>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_voting, menu);
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

    public void onCreateVote(View v) {
        /*
        Create the actual vote object
        Add to the list and local database
        Call the remote server
         */
    }

    public void onVoteOption(View v) {
        /*
        Make necessary changes to the local vote objects
        Call the remote server
         */
    }

    public void onRefreshVote(View v) {
        /*
        Pull the up-to-date votes visible to the current user, and update the cache if necessary
        Clear the list, and reload from the cache database
         */
    }
}
