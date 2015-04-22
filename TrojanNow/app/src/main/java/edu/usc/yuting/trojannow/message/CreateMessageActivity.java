package edu.usc.yuting.trojannow.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import edu.usc.yuting.trojannow.R;
import edu.usc.yuting.trojannow.common.SendIntent;
import edu.usc.yuting.trojannow.common.UpdateUI;
import edu.usc.yuting.trojannow.login.User;

public class CreateMessageActivity extends ActionBarActivity implements UpdateUI, SendIntent{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);
        User.getInstance().refreshUser(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void populateSpinner() {
        Spinner spinner = (Spinner)findViewById(R.id.receiverSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, new ArrayList<String>());
        for (User user : User.getInstance().getUsers()) {
            adapter.add(user.getUserName());
        }
        spinner.setAdapter(adapter);
    }

    public void onUploadMessage(View v) {
        Spinner spinner = (Spinner)findViewById(R.id.receiverSpinner);
        String uid = User.getInstance().getId(spinner.getSelectedItem().toString());
        if (uid != null) {
            EditText text = (EditText)findViewById(R.id.createMessageEditText);
            MessageRepository.getInstance().uploadMessage(uid, text.getText().toString(), this);
        }
    }

    public void onCancelUploadMessage(View v) {
        onSendIntent();
    }

    @Override
    public void onUpdateUI() {
        populateSpinner();
    }

    @Override
    public void onSendIntent() {
        Intent intent = new Intent(this, MessageActivity.class);
        startActivity(intent);
    }
}
