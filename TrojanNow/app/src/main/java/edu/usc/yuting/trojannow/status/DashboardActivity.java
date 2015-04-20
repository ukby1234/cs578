package edu.usc.yuting.trojannow.status;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.usc.yuting.trojannow.Intents;
import edu.usc.yuting.trojannow.R;
import edu.usc.yuting.trojannow.login.User;

public class DashboardActivity extends ActionBarActivity {

    private class DashboardTask extends AsyncTask<String, Void, Void> {
        private ActionBarActivity activity;
        List<edu.usc.yuting.trojannow.status.Status> posts = new ArrayList<edu.usc.yuting.trojannow.status.Status>();
        public DashboardTask(ActionBarActivity activity) {
            this.activity = activity;
        }
        @Override
        protected Void doInBackground(String... uid) {
            User user = User.getInstance();
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet("http://10.0.2.2:8000/post/" + user.getUid() + "/");
                HttpResponse response = client.execute(get);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseText = EntityUtils.toString(response.getEntity());
                    JSONArray results = new JSONArray(responseText);
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject obj = results.getJSONObject(i);
                        posts.add(new edu.usc.yuting.trojannow.status.Status(obj.getString("id"), obj.getString("user"),obj.getString("uid"), obj.getString("text"), obj.getBoolean("anonymous")));
                    }
                }
            }catch(Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.dashboardLayout);
            linearLayout.removeAllViewsInLayout();
            for (final edu.usc.yuting.trojannow.status.Status status : posts) {
                try {
                    LinearLayout innerLayout = new LinearLayout(activity);
                    innerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onDisplay(v, status);
                        }
                    });
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    TextView text = new TextView(activity);
                    text.setText("\t\t" + status.getText());
                    TextView username = new TextView(activity);
                    username.setText(status.getUsername() + ": ");
                    innerLayout.addView(username);
                    innerLayout.addView(text);
                    username.setLayoutParams(params);
                    text.setLayoutParams(params);
                    linearLayout.addView(innerLayout);
                }catch(Exception e) {

                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        new DashboardTask(this).execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onDisplay(View v, Status status) {
        Intent intent = new Intent(this, StatusActivity.class);
        intent.putExtra(Intents.intents.get("STATUS_INTENT"), status);
        startActivity(intent);
    }

    public void onRefreshStatus(View v) {
        /*
        Update the local cache database if necessary
        Clear the list cache, and reload from the cache database
         */
        new DashboardTask(this).execute();
    }

    public void onCreateStatus(View v) {
        /*
        Create the actual status
        Create both locally and remotely
         */
        Intent intent = new Intent(this, CreateStatusActivity.class);
        intent.putExtra(Intents.intents.get("SENSOR_CLEAR_INTENT"), true);
        startActivity(intent);
    }
}
