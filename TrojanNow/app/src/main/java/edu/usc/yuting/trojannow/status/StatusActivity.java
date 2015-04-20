package edu.usc.yuting.trojannow.status;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.usc.yuting.trojannow.Intents;
import edu.usc.yuting.trojannow.R;
import edu.usc.yuting.trojannow.comment.Comment;
import edu.usc.yuting.trojannow.login.User;
import edu.usc.yuting.trojannow.sensor.Sensor;

public class StatusActivity extends ActionBarActivity {
    List<Comment> comments = new ArrayList<Comment>();
    List<Sensor> sensors = new ArrayList<Sensor>();
    Status status;
    private class GetCommentTask extends AsyncTask<String, Void, Void> {
        private ActionBarActivity activity;
        public GetCommentTask(ActionBarActivity activity) {
            this.activity = activity;
        }
        @Override
        protected Void doInBackground(String... pid) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet("http://10.0.2.2:8000/comment/" + pid[0] + "/");
                HttpResponse response = client.execute(get);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseText = EntityUtils.toString(response.getEntity());
                    JSONArray results = new JSONArray(responseText);
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject obj = results.getJSONObject(i);
                        comments.add(new Comment(obj.getString("id"), obj.getString("user"), obj.getString("text")));
                    }
                }
                get = new HttpGet("http://10.0.2.2:8000/attribute/" + pid[0] + "/");
                response = client.execute(get);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseText = EntityUtils.toString(response.getEntity());
                    JSONArray results = new JSONArray(responseText);
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject obj = results.getJSONObject(i);
                        sensors.add(new Sensor(obj.getString("id"), obj.getString("source"), obj.getString("information")));
                    }
                }
            }catch(Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            redrawEnvironmentAttributes();
            redrawComments();
        }
    }

    private class DeletePostTask extends AsyncTask<String, Void, Void> {
        private ActionBarActivity activity;
        public DeletePostTask(ActionBarActivity activity) {
            this.activity = activity;
        }
        @Override
        protected Void doInBackground(String... pid) {
            User user = User.getInstance();
            try {
                HttpClient client = new DefaultHttpClient();
                HttpDelete delete = new HttpDelete("http://10.0.2.2:8000/post/" + user.getUid() + "/" + pid[0] + "/");
                client.execute(delete);
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

    private class CreateCommentTask extends AsyncTask<String, Void, Void> {
        private ActionBarActivity activity;
        public CreateCommentTask(ActionBarActivity activity) {
            this.activity = activity;
        }
        @Override
        protected Void doInBackground(String... pid) {
            User user = User.getInstance();
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://10.0.2.2:8000/comment/" + pid[0] + "/");
                client.execute(post);
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

    private void redrawEnvironmentAttributes() {
        LinearLayout attributeLayout = (LinearLayout)findViewById(R.id.statusAttributeLayout);
        attributeLayout.removeAllViewsInLayout();
        for (Sensor sensor : sensors) {
            try {
                LinearLayout innerLayout = new LinearLayout(this);
                innerLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                TextView information = new TextView(this);
                information.setText("\t\t" + sensor.getInformation());
                TextView source = new TextView(this);
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
        LinearLayout commentLayout = (LinearLayout)findViewById(R.id.statusCommentLayout);
        commentLayout.removeAllViewsInLayout();
        for (Comment comment : comments) {
            try {
                LinearLayout innerLayout = new LinearLayout(this);
                innerLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                TextView text = new TextView(this);
                text.setText("\t\t" + comment.getText());
                TextView username = new TextView(this);
                username.setText(comment.getUserName() + ": ");
                innerLayout.addView(username);
                innerLayout.addView(text);
                username.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                innerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                commentLayout.addView(innerLayout);
            }catch(Exception e) {

            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Intent intent = getIntent();
        status = (Status)intent.getSerializableExtra(Intents.intents.get("STATUS_INTENT"));
        TextView userName = (TextView)findViewById(R.id.statusUserNameText);
        userName.setText(status.getUsername() + ": ");
        TextView content = (TextView)findViewById(R.id.statusContentText);
        content.setText("\t\t" + status.getText());
        Button button = (Button)findViewById(R.id.deleteStatusButton);
        if (User.getInstance().getUid().equals(status.getUid()) ) {
            button.setVisibility(Button.VISIBLE);
        }
        else {
            button.setVisibility(Button.GONE);
        }
        new GetCommentTask(this).execute(status.getSid());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onDeleteStatus(View v) {
        /*
        Find the corresponding status
        Delete both locally and remotely
         */
        new DeletePostTask(this).execute(status.getSid());
    }

    public void onCreateComment(View v) {
        /*
        Find the corresponding status
        Delete both locally and remotely
         */
        new DeletePostTask(this).execute(status.getSid());
    }

    public void onUpdateStatus(View v) {
        /*
        Find the corresponding status
        Change the text inside the status
        Update both locally and remotely
         */
    }


}
