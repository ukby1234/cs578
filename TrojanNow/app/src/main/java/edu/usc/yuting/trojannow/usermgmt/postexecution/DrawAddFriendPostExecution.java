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
import edu.usc.yuting.trojannow.usermgmt.abstraction.FriendCandidate;

/**
 * Created by chengyey on 4/30/15.
 */

public class DrawAddFriendPostExecution extends PostExecution{

    public DrawAddFriendPostExecution(Activity activity) {
        super(activity);
    }

    @Override
    public void onPostExecution() {
        List<FriendCandidate> candidates = FriendRepository.getInstance().getCandidates();
        LinearLayout layout = (LinearLayout)activity.findViewById(R.id.addFriendLayout);
        layout.removeAllViewsInLayout();
        for (final FriendCandidate candidate : candidates) {
            LinearLayout inner = new LinearLayout(activity);
            inner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            inner.setOrientation(LinearLayout.HORIZONTAL);
            TextView userName = new TextView(activity);
            userName.setText(candidate.getUserName());
            Button button = new Button(activity);
            button.setText("Add");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFriend(candidate.getUid());
                }
            });
            int padding = (int)activity.getResources().getDimension(R.dimen.result_padding);
            userName.setPadding(padding, padding, padding, padding);
            button.setPadding(padding, padding, padding, padding);
            inner.addView(userName);
            inner.addView(button);
            layout.addView(inner);
        }
    }

    private void addFriend(String friendId) {
        FriendRepository.getInstance().addFriend(friendId, this);
    }
}
