package edu.usc.yuting.trojannow.message.postexecution;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import edu.usc.yuting.trojannow.R;
import edu.usc.yuting.trojannow.common.PostExecution;
import edu.usc.yuting.trojannow.database.FriendRepository;
import edu.usc.yuting.trojannow.usermgmt.abstraction.FriendUser;

/**
 * Created by chengyey on 4/30/15.
 */
public class PopulateSpinnerPostExecution extends PostExecution{

    public PopulateSpinnerPostExecution(Activity activity) {
        super(activity);
    }

    @Override
    public void onPostExecution() {
        Spinner spinner = (Spinner)activity.findViewById(R.id.receiverSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_item, new ArrayList<String>());
        for (FriendUser user : FriendRepository.getInstance().getFriends()) {
            if (user.isAccepted()) {
                adapter.add(user.getUserName());
            }
        }
        spinner.setAdapter(adapter);
    }
}
