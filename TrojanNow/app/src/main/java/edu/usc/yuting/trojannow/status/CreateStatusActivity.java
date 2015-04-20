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
import edu.usc.yuting.trojannow.login.User;
import edu.usc.yuting.trojannow.sensor.Sensor;
import edu.usc.yuting.trojannow.sensor.SensorActivity;

public class CreateStatusActivity extends ActionBarActivity {
    private static List<Sensor> sensors = new ArrayList<Sensor>();
    private  Map<Button, Sensor> buttonMapping = new HashMap<Button, Sensor>();
    static String text = "";
    private class CreateStatusTask extends AsyncTask<String, Void, Void> {
        private ActionBarActivity activity;
        public CreateStatusTask(ActionBarActivity activity) {
            this.activity = activity;
        }
        @Override
        protected Void doInBackground(String... uid) {
            User user = User.getInstance();
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://10.0.2.2:8000/post/" + user.getUid() + "/");
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair("text", uid[0]));
                pairs.add(new BasicNameValuePair("anonymous", uid[1]));
                post.setEntity(new UrlEncodedFormEntity(pairs));
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseText = EntityUtils.toString(response.getEntity());
                    JSONObject results = new JSONObject(responseText);
                    String pid = results.getString("post_id");
                    for (Sensor sensor : sensors) {
                        HttpPost sensorPost = new HttpPost("http://10.0.2.2:8000/attribute/" + pid + "/environment/");
                        List<NameValuePair> sensorPairs = new ArrayList<NameValuePair>();
                        sensorPairs.add(new BasicNameValuePair("source", sensor.getSource()));
                        sensorPairs.add(new BasicNameValuePair("information", sensor.getInformation()));
                        sensorPost.setEntity(new UrlEncodedFormEntity(sensorPairs));
                        responseText = EntityUtils.toString(client.execute(sensorPost).getEntity());
                        results = new JSONObject(responseText);
                        sensor.setId(results.getString("attribute_id"));
                    }
                }
            }catch(Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Intent intent = new Intent(activity, DashboardActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_status);
        Intent intent = getIntent();
        if (intent.hasExtra(Intents.intents.get("SENSOR_INTENT"))) {
            Sensor sensor = (Sensor)intent.getSerializableExtra(Intents.intents.get("SENSOR_INTENT"));
            sensors.add(sensor);
        }
        if (intent.hasExtra(Intents.intents.get("SENSOR_CLEAR_INTENT"))) {
            sensors.clear();
            buttonMapping.clear();
            text = "";
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
        String anonymous = "" + ((CheckBox)findViewById(R.id.anonymousCheckBox)).isChecked();
        if (text != null && !text.isEmpty()) {
            new CreateStatusTask(this).execute(text, anonymous);
        }
    }

    public void onCreateSensor(View v) {
        text = ((EditText)findViewById(R.id.statusText)).getText().toString();
        Intent intent = new Intent(this, SensorActivity.class);
        startActivity(intent);
    }

    public void onCancelSensor(View v) {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    private void redrawLayout(final Context context) {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.sensorListLayout);
        linearLayout.removeAllViewsInLayout();
        EditText text = (EditText)findViewById(R.id.statusText);
        text.setText(this.text);
        for (Sensor sensor : sensors) {
            LinearLayout r = new LinearLayout(context);
            Button button = new Button(context);
            button.setText("Delete");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Sensor s = buttonMapping.get((Button)v);
                    if (s != null) {
                        sensors.remove(s);
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
}
