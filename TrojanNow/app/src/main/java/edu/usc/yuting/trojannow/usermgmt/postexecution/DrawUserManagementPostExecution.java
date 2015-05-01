package edu.usc.yuting.trojannow.usermgmt.postexecution;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import edu.usc.yuting.trojannow.R;
import edu.usc.yuting.trojannow.common.PostExecution;
import edu.usc.yuting.trojannow.database.FriendRepository;
import edu.usc.yuting.trojannow.usermgmt.abstraction.FriendUser;

/**
 * Created by chengyey on 4/30/15.
 */

public class DrawUserManagementPostExecution extends PostExecution{

    public DrawUserManagementPostExecution(Activity activity) {
        super(activity);
    }

    @Override
    public void onPostExecution() {
        List<FriendUser> users = FriendRepository.getInstance().getFriends();
        LinearLayout layout = (LinearLayout)activity.findViewById(R.id.friendListLayout);
        layout.removeAllViewsInLayout();
        for (final FriendUser user : users) {
            LinearLayout inner = new LinearLayout(activity);
            inner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            inner.setOrientation(LinearLayout.HORIZONTAL);
            TextView userName = new TextView(activity);
            userName.setText(user.getUserName());
            int padding = (int)activity.getResources().getDimension(R.dimen.result_padding);
            userName.setPadding(padding, padding, padding, padding);
            inner.addView(userName);
            if (user.isAccepted()) {
                Button button = new Button(activity);
                button.setText("Delete");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteFriend(user.getUid());
                    }
                });
                button.setPadding(padding, padding, padding, padding);
                inner.addView(button);


            }
            else if (user.getDirection()){
                Button button = new Button(activity);
                button.setText("Approve");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        approveFriend(user.getUid());
                    }
                });
                button.setPadding(padding, padding, padding, padding);
                inner.addView(button);

            }
            else {
                TextView text = new TextView(activity);
                text.setText("Waiting Approval");
                text.setPadding(padding, padding, padding, padding);
                inner.addView(text);
            }
            layout.addView(inner);
        }
    }

    private void approveFriend(String friendId) {
        FriendRepository.getInstance().approveFriend(friendId, this);
    }

    private void deleteFriend(String friendId) {
        FriendRepository.getInstance().deleteFriend(friendId, this);
    }
}
