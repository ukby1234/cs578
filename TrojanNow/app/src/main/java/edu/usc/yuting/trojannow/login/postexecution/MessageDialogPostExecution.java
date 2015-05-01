package edu.usc.yuting.trojannow.login.postexecution;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;

import edu.usc.yuting.trojannow.R;
import edu.usc.yuting.trojannow.common.PostExecution;

/**
 * Created by chengyey on 4/30/15.
 */
public class MessageDialogPostExecution extends PostExecution{
    private String title;
    private String content;

    public MessageDialogPostExecution(Activity activity) {
        super(activity);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void onPostExecution() {
        ((EditText)activity.findViewById(R.id.userNameText)).setText("");
        ((EditText)activity.findViewById(R.id.passwordText)).setText("");
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
