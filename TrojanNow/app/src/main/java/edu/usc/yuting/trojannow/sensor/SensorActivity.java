package edu.usc.yuting.trojannow.sensor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.LinkedList;
import java.util.List;

import edu.usc.yuting.trojannow.Intents;
import edu.usc.yuting.trojannow.R;
import edu.usc.yuting.trojannow.status.CreateStatusActivity;
import edu.usc.yuting.trojannow.status.Status;

public class SensorActivity extends ActionBarActivity {
    Status status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        Intent intent = getIntent();
        if (intent.hasExtra(Intents.intents.get("STATUS_INTENT"))) {
            status = (Status)intent.getSerializableExtra(Intents.intents.get("STATUS_INTENT"));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onAttachSensorData(View v) {
        /*
        Collect the required sensor data
        Update the local cache database and list
        Upload to the server
         */
        String source = ((Spinner)findViewById(R.id.sensorSourceSpinner)).getSelectedItem().toString();
        String information = ((EditText)findViewById(R.id.sensorInformationEditText)).getText().toString();
        Sensor sensor = new Sensor(source, information);
        Intent intent = new Intent(this, CreateStatusActivity.class);
        intent.putExtra(Intents.intents.get("SENSOR_INTENT"), sensor);
        intent.putExtra(Intents.intents.get("STATUS_INTENT"), status);
        startActivity(intent);
    }

    public void onCancelSensorData(View v) {
        /*
        Find the corresponding one to delete
        Delete both locally and remotely
         */
        Intent intent = new Intent(this, CreateStatusActivity.class);
        intent.putExtra(Intents.intents.get("STATUS_INTENT"), status);
        startActivity(intent);
    }
}
