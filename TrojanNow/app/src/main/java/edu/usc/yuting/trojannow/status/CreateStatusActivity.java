package edu.usc.yuting.trojannow.status;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.usc.yuting.trojannow.Intents;
import edu.usc.yuting.trojannow.R;
import edu.usc.yuting.trojannow.SendIntent;
import edu.usc.yuting.trojannow.login.User;
import edu.usc.yuting.trojannow.sensor.Sensor;
import edu.usc.yuting.trojannow.sensor.SensorActivity;
import edu.usc.yuting.trojannow.sensor.SensorRepository;

public class CreateStatusActivity extends ActionBarActivity implements SendIntent{
    private  Map<Button, Sensor> buttonMapping = new HashMap<Button, Sensor>();
    static Status status = null;

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
        status.setAnonymous(anonymous);
        status.setText(text);
        if (text != null && !text.isEmpty()) {
            StatusRepository.getInstance().createStatus(status, this);
            status = null;
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
        status = null;
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
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
            TextView source = new TextView(context);
            source.setText(sensor.getSource());
            source.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 0.5f));
            TextView information = new TextView(context);
            information.setText(sensor.getInformation());
            information.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
            r.addView(button);
            r.addView(source);
            r.addView(information);
            linearLayout.addView(r);
        }
    }

    @Override
    public void onSendIntent() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }
}
