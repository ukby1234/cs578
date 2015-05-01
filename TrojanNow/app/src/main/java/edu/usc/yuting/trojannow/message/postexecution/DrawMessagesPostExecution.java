package edu.usc.yuting.trojannow.message.postexecution;

import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import edu.usc.yuting.trojannow.R;
import edu.usc.yuting.trojannow.common.PostExecution;
import edu.usc.yuting.trojannow.database.MessageRepository;
import edu.usc.yuting.trojannow.message.abstraction.Message;

/**
 * Created by chengyey on 4/30/15.
 */
public class DrawMessagesPostExecution extends PostExecution{
    public DrawMessagesPostExecution(Activity activity) {
        super(activity);
    }

    @Override
    public void onPostExecution() {
        LinearLayout layout = (LinearLayout)activity.findViewById(R.id.messageLayout);
        layout.removeAllViews();
        for (Message message : MessageRepository.getInstance().getMessages()) {
            int padding = (int)activity.getResources().getDimension(R.dimen.table_padding);
            TextView metadata = new TextView(activity);
            metadata.setText("From " + message.getSenderName() + " to " + message.getReceiverName() + ":");
            metadata.setPadding(padding, padding, padding, padding);
            TextView text = new TextView(activity);
            text.setText(message.getText());
            text.setPadding(padding, padding, padding, padding);
            TextView date = new TextView(activity);
            date.setText(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(message.getDate()));
            date.setPadding(padding, padding, padding, padding);
            layout.addView(metadata);
            layout.addView(text);
            layout.addView(date);
        }
    }
}
