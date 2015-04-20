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
import edu.usc.yuting.trojannow.status.DashboardActivity;


public class LoginActivity extends ActionBarActivity {
    private class LoginTask extends AsyncTask<Void, Void, Void> {
        private ActionBarActivity activity;
        User user;
        public LoginTask(ActionBarActivity activity) {
            this.activity = activity;
        }
        @Override
        protected Void doInBackground(Void... urls) {
            String userName = ((EditText)findViewById(R.id.userNameText)).getText().toString();
            String password = ((EditText)findViewById(R.id.passwordText)).getText().toString();
            user = User.authenticate(userName, password);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (user != null) {
                Intent intent = new Intent(activity, DashboardActivity.class);
                startActivity(intent);
            }
        }
    }
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
        new LoginTask(this).execute();
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
}
