package edu.usc.yuting.trojannow.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import edu.usc.yuting.trojannow.R;
import edu.usc.yuting.trojannow.UpdateUI;
import edu.usc.yuting.trojannow.status.DashboardActivity;

public class MessageActivity extends ActionBarActivity implements UpdateUI{
    List<Message> messages = new LinkedList<Message>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        MessageRepository.getInstance().refreshMessage(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_dashboard) {
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSendMessage(View v) {
        /*
        First construct the local object for a message
        Then add to the activity class and the local cache database
        Finally, make the remote call to the server
         */
        Intent intent = new Intent(this, CreateMessageActivity.class);
        startActivity(intent);
    }

    public void onRefreshMessage(View v) {
        /*
        Update the local cache database if there is an Internet connection
        Clear the list and load everything from the cache database
         */
        MessageRepository.getInstance().refreshMessage(this);
    }

    private void drawMessages() {
        LinearLayout layout = (LinearLayout)findViewById(R.id.messageLayout);
        layout.removeAllViews();
        for (Message message : MessageRepository.getInstance().getMessages()) {
            TextView metadata = new TextView(this);
            metadata.setText("From " + message.getSenderName() + " to " + message.getReceiverName() + ": ");
            TextView text = new TextView(this);
            text.setText(message.getText());
            layout.addView(metadata);
            layout.addView(text);
        }
    }


    @Override
    public void onUpdateUI() {
        drawMessages();
    }
}
