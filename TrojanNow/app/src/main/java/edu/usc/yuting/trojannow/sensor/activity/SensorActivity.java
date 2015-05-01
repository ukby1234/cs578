package edu.usc.yuting.trojannow.sensor.activity;

import android.content.Intent;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.usc.yuting.trojannow.R;
import edu.usc.yuting.trojannow.common.Intents;
import edu.usc.yuting.trojannow.database.SensorRepository;
import edu.usc.yuting.trojannow.sensor.abstraction.Sensor;
import edu.usc.yuting.trojannow.status.abstraction.Status;
import edu.usc.yuting.trojannow.status.activity.CreateStatusActivity;

public class SensorActivity extends ActionBarActivity implements SensorEventListener, AdapterView.OnItemSelectedListener {
    Status status;
    String accelerometer = "";
    String GPS = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        Intent intent = getIntent();
        if (intent.hasExtra(Intents.intents.get("STATUS_INTENT"))) {
            status = (Status)intent.getSerializableExtra(Intents.intents.get("STATUS_INTENT"));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, new ArrayList<String>());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        List<String> sources = new LinkedList<>();
        for (CharSequence seq : getResources().getTextArray(R.array.sensor_arrays)) {
            sources.add(seq.toString());
        }
        if (status != null) {
            List<Sensor> sensors = SensorRepository.getInstance().getLocalSensors(status);
            for (Sensor s : sensors) {
                sources.remove(s.getSource());
            }
        }
        dataAdapter.addAll(sources);
        ((Spinner) findViewById(R.id.sensorSourceSpinner)).setAdapter(dataAdapter);
        SensorManager sMgr = (SensorManager)this.getSystemService(SENSOR_SERVICE);
        android.hardware.Sensor accelerometer = sMgr.getDefaultSensor(android.hardware.Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sMgr.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
        ((Spinner)findViewById(R.id.sensorSourceSpinner)).setOnItemSelectedListener(this);
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
        String source = ((Spinner) findViewById(R.id.sensorSourceSpinner)).getSelectedItem().toString();
        String information = "";
        if (source.equals("Accelerometer")) {
            information = accelerometer;
        }
        else if (source.equals("GPS")) {
            information = GPS;
        }
        Sensor sensor = new Sensor(source, information);
        Intent intent = new Intent(this, CreateStatusActivity.class);
        if (source != null && !source.isEmpty()) {
            intent.putExtra(Intents.intents.get("SENSOR_INTENT"), sensor);
        }
        intent.putExtra(Intents.intents.get("STATUS_INTENT"), status);
        startActivity(intent);
    }

    public void onCancelSensorData(View v) {
        /*
        Find the corresponding one to delete
        Delete both locally and remotely
         */
        sendCreateStatusIntent();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            sendCreateStatusIntent();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void sendCreateStatusIntent() {
        Intent intent = new Intent(this, CreateStatusActivity.class);
        intent.putExtra(Intents.intents.get("STATUS_INTENT"), status);
        startActivity(intent);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        accelerometer = "";
        boolean isFirst = true;
        for (float num : event.values) {
            if (isFirst) {
                accelerometer += num;
                isFirst = false;
            }
            else {
                accelerometer += ", " + num;
            }
        }
        try {
            Spinner spinner = (Spinner)findViewById(R.id.sensorSourceSpinner);
            if (spinner.getSelectedItem().equals("Accelerometer")) {
                TextView sensorData = (TextView) findViewById(R.id.sensorDataTextView);
                sensorData.setText(accelerometer);
            }
        }catch (Exception e) {

        }

    }

    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView sensorData = (TextView)findViewById(R.id.sensorDataTextView);
        if (parent.getAdapter().getItem(position).toString().equals("GPS")) {
            LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc != null) {
                GPS = "Lat: " + loc.getLatitude() + " Lon: " + loc.getLongitude();
                sensorData.setText(GPS);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
