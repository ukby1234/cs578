package edu.usc.yuting.trojannow.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import edu.usc.yuting.trojannow.Intents;
import edu.usc.yuting.trojannow.R;
import edu.usc.yuting.trojannow.SendIntent;
import edu.usc.yuting.trojannow.status.DashboardActivity;


public class LoginActivity extends ActionBarActivity implements SendIntent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onLogin(View v) {
        /*
        contact the remote server to authenticate with credentials
        If successful, update the local database cache with the new user information
        Also, update create the User class variable, and put into the CacheDatabase instance
         */
        String userName = ((EditText)findViewById(R.id.userNameText)).getText().toString();
        String password = ((EditText)findViewById(R.id.passwordText)).getText().toString();
        User.authenticate(userName, password, this);
    }

    public void onResetPassword(View v) {
        /*
        Provide functionality to contact the remote server for resetting password
         */
    }

    public void onCreateUser(View v) {
        /*
        After the user fills out the new user form, it will request the remote server for a new user creation
         */
    }

    @Override
    public void onSendIntent() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }
}
