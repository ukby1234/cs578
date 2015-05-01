package edu.usc.yuting.trojannow.status.postexecution;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import edu.usc.yuting.trojannow.R;
import edu.usc.yuting.trojannow.comment.abstraction.Comment;
import edu.usc.yuting.trojannow.common.PostExecution;
import edu.usc.yuting.trojannow.database.CommentRepository;
import edu.usc.yuting.trojannow.login.abstraction.User;
import edu.usc.yuting.trojannow.sensor.abstraction.Sensor;
import edu.usc.yuting.trojannow.database.SensorRepository;
import edu.usc.yuting.trojannow.status.abstraction.Status;

/**
 * Created by chengyey on 4/30/15.
 */

public class DrawStatusActivityPostExecution extends PostExecution{

    private Status status;

    public DrawStatusActivityPostExecution(Activity activity, Status status) {
        super(activity);
        this.status = status;
    }

    @Override
    public void onPostExecution() {
        redrawEnvironmentAttributes();
        redrawComments();
    }

    private void redrawEnvironmentAttributes() {
        LinearLayout attributeLayout = (LinearLayout)activity.findViewById(R.id.statusAttributeLayout);
        attributeLayout.removeAllViewsInLayout();
        for (Sensor sensor : SensorRepository.getInstance().getSensorsFromStatusId(status.getSid())) {
            try {
                LinearLayout innerLayout = new LinearLayout(activity);
                innerLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                TextView information = new TextView(activity);
                information.setText("\t\t" + sensor.getInformation());
                TextView source = new TextView(activity);
                source.setText(sensor.getSource() + ": ");
                innerLayout.addView(source);
                innerLayout.addView(information);
                source.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                information.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                innerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                attributeLayout.addView(innerLayout);
            }catch(Exception e) {

            }
        }
    }

    private void redrawComments() {
        LinearLayout commentLayout = (LinearLayout)activity.findViewById(R.id.statusCommentLayout);
        commentLayout.removeAllViewsInLayout();
        for (final Comment comment : CommentRepository.getInstance().getCommentsFromStatusId(status.getSid())) {
            try {
                int padding = (int)activity.getResources().getDimension(R.dimen.table_padding);
                LinearLayout innerLayout = new LinearLayout(activity);
                innerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextView text = new TextView(activity);

                text.setText(comment.getText());
                TextView username = new TextView(activity);
                username.setText(comment.getUserName() + ":");
                username.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                innerLayout.addView(username);
                innerLayout.addView(text);
                username.setPadding(padding, padding, padding, padding);
                text.setPadding(padding, padding, padding, padding);
                commentLayout.addView(innerLayout);
                innerLayout = new LinearLayout(activity);
                TextView date = new TextView(activity);
                date.setText(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(comment.getDate()));
                date.setPadding(padding, padding, padding, padding);
                innerLayout.addView(date);
                if (comment.getUserId().equals(User.getInstance().getUid())) {
                    Button delete = new Button(activity);
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onDeleteComment(comment.getId());
                        }
                    });
                    delete.setText("Delete");
                    delete.setPadding(padding, padding, padding, padding);
                    innerLayout.addView(delete);
                }
                commentLayout.addView(innerLayout);
            }catch(Exception e) {

            }
        }
    }

    private void onDeleteComment(String cid) {
        /*
        Find the corresponding status
        Change the text inside the status
        Update both locally and remotely
         */
        CommentRepository.getInstance().deleteComment(cid, this);
    }
}
