package edu.usc.yuting.trojannow.login.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import edu.usc.yuting.trojannow.R;
import edu.usc.yuting.trojannow.login.abstraction.User;
import edu.usc.yuting.trojannow.login.postexecution.DashboardActivityIntentPostExecution;
import edu.usc.yuting.trojannow.login.postexecution.MessageDialogPostExecution;


public class LoginActivity extends ActionBarActivity {

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onLogin(View v) {
        /*
        contact the remote server to authenticate with credentials
        If successful, update the local database cache with the new user information
        Also, update create the User class variable, and put into the CacheDatabase instance
         */
        String userName = ((EditText)findViewById(R.id.userNameText)).getText().toString();
        String password = ((EditText)findViewById(R.id.passwordText)).getText().toString();
        User.authenticate(userName, password, new DashboardActivityIntentPostExecution(this));
    }

    public void onCreateUser(View v) {
        /*
        After the user fills out the new user form, it will request the remote server for a new user creation
         */
        String userName = ((EditText)findViewById(R.id.userNameText)).getText().toString();
        String password = ((EditText)findViewById(R.id.passwordText)).getText().toString();
        if (userName != null && !userName.isEmpty() && password != null && !password.isEmpty()) {
            User.createUser(userName, password, new MessageDialogPostExecution(this));
        }
        else {
            new AlertDialog.Builder(this)
                    .setTitle("Create User")
                    .setMessage("User name and password can't be empty! ")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    public void onClearUser(View v) {
        ((EditText)findViewById(R.id.userNameText)).setText("");
        ((EditText)findViewById(R.id.passwordText)).setText("");
    }
}
