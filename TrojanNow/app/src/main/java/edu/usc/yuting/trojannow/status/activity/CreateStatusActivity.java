package edu.usc.yuting.trojannow.status.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import edu.usc.yuting.trojannow.R;
import edu.usc.yuting.trojannow.common.Intents;
import edu.usc.yuting.trojannow.database.StatusRepository;
import edu.usc.yuting.trojannow.login.abstraction.User;
import edu.usc.yuting.trojannow.login.postexecution.DashboardActivityIntentPostExecution;
import edu.usc.yuting.trojannow.sensor.abstraction.Sensor;
import edu.usc.yuting.trojannow.database.SensorRepository;
import edu.usc.yuting.trojannow.sensor.activity.SensorActivity;
import edu.usc.yuting.trojannow.status.abstraction.Status;

public class CreateStatusActivity extends ActionBarActivity{
    private  Map<Button, Sensor> buttonMapping = new HashMap<Button, Sensor>();
    private Status status = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_status);
        Intent intent = getIntent();
        if (intent.hasExtra(Intents.intents.get("STATUS_INTENT"))) {
            status = (Status)intent.getSerializableExtra(Intents.intents.get("STATUS_INTENT"));
        }
        else {
            status = null;
        }
        if (intent.hasExtra(Intents.intents.get("SENSOR_INTENT"))) {
            Sensor sensor = (Sensor)intent.getSerializableExtra(Intents.intents.get("SENSOR_INTENT"));
            SensorRepository.getInstance().addLocalSensor(status, sensor);
        }
        if (status != null && SensorRepository.getInstance().getLocalSensors(status).size() == getResources().getTextArray(R.array.sensor_arrays).length) {
            ((Button)findViewById(R.id.attachEnvironmentInformation)).setVisibility(View.INVISIBLE);
        }
        else {
            ((Button)findViewById(R.id.attachEnvironmentInformation)).setVisibility(View.VISIBLE);
        }
        redrawLayout(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onUploadStatus(View v) {
        /*
        Create the actual status
        Create both locally and remotely
         */
        String text = ((EditText)findViewById(R.id.statusText)).getText().toString();
        boolean anonymous =((CheckBox)findViewById(R.id.anonymousCheckBox)).isChecked();
        if (status == null) {
            status = new Status(User.getInstance().getUid(), text, anonymous);
        }
        status.setAnonymous(anonymous);
        status.setText(text);
        if (text != null && !text.isEmpty()) {
            StatusRepository.getInstance().createStatus(status, new DashboardActivityIntentPostExecution(this));
            status = null;
        }
        else {
            new AlertDialog.Builder(this)
                    .setTitle("Create Status")
                    .setMessage("Text can't be empty! ")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    public void onCreateSensor(View v) {
        String text = ((EditText)findViewById(R.id.statusText)).getText().toString();
        boolean isAnonymous = ((CheckBox)findViewById(R.id.anonymousCheckBox)).isChecked();
        if (status == null) {
            status = new Status(User.getInstance().getUid(), text,isAnonymous );
        }
        else {
            status.setText(text);
            status.setAnonymous(isAnonymous);
        }
        Intent intent = new Intent(this, SensorActivity.class);
        intent.putExtra(Intents.intents.get("STATUS_INTENT"), status);
        startActivity(intent);
    }

    public void onCancelSensor(View v) {
        sendDashboardIntent();
    }

    public void sendDashboardIntent() {
        status = null;
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            sendDashboardIntent();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void redrawLayout(final Context context) {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.sensorListLayout);
        linearLayout.removeAllViewsInLayout();
        if (status != null) {
            ((EditText)findViewById(R.id.statusText)).setText(status.getText());
            ((CheckBox)findViewById(R.id.anonymousCheckBox)).setChecked(status.isAnonymous());
        }
        for (Sensor sensor : SensorRepository.getInstance().getLocalSensors(status)) {
            LinearLayout r = new LinearLayout(context);
            Button button = new Button(context);
            button.setText("Delete");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Sensor s = buttonMapping.get((Button)v);
                    if (s != null) {
                        SensorRepository.getInstance().removeSensor(status, s);
                        redrawLayout(context);
                    }
                }
            });
            buttonMapping.put(button, sensor);
            int padding = (int)getResources().getDimension(R.dimen.result_padding);
            TextView source = new TextView(context);
            source.setText(sensor.getSource());
            TextView information = new TextView(context);
            information.setText(sensor.getInformation());
            button.setPadding(padding, padding, padding, padding);
            source.setPadding(padding, padding, padding, padding);
            information.setPadding(padding, padding, padding, padding);
            r.addView(button);
            r.addView(source);
            r.addView(information);
            linearLayout.addView(r);
        }
    }

}
