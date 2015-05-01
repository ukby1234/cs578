package edu.usc.yuting.trojannow.message.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import edu.usc.yuting.trojannow.R;
import edu.usc.yuting.trojannow.database.FriendRepository;
import edu.usc.yuting.trojannow.database.MessageRepository;
import edu.usc.yuting.trojannow.message.postexecution.MessageActivityIntentPostExecution;
import edu.usc.yuting.trojannow.message.postexecution.PopulateSpinnerPostExecution;

public class CreateMessageActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);
        FriendRepository.getInstance().updateFriendList(new PopulateSpinnerPostExecution(this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onUploadMessage(View v) {
        Spinner spinner = (Spinner)findViewById(R.id.receiverSpinner);
        String uid = null;
        try {
            uid = FriendRepository.getInstance().getId(spinner.getSelectedItem().toString());
        }catch (Exception e) {

        }
        EditText text = (EditText)findViewById(R.id.createMessageEditText);
        if (text.getText().toString() == null || text.getText().toString().isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle("Create Message")
                    .setMessage("Text can't be empty! ")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else if (uid == null || uid.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle("Create Message")
                    .setMessage("No Friends to sent! ")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else {
            MessageRepository.getInstance().uploadMessage(uid, text.getText().toString(), new MessageActivityIntentPostExecution(this));
        }
    }

    public void onCancelUploadMessage(View v) {
        new MessageActivityIntentPostExecution(this).onPostExecution();
    }

}
