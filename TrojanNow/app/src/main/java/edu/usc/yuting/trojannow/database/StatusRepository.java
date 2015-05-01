package edu.usc.yuting.trojannow.database;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.usc.yuting.trojannow.R;
import edu.usc.yuting.trojannow.common.PostExecution;
import edu.usc.yuting.trojannow.login.abstraction.User;
import edu.usc.yuting.trojannow.status.abstraction.Status;

/**
 * Created by Frank on 4/21/2015.
 */
public class StatusRepository {
    private Map<String, Status> statuses = new HashMap<String, Status>();
    private static StatusRepository instance = new StatusRepository();
    private StatusRepository() {

    }

    private class RefreshStatusTask extends AsyncTask<String, Void, Void> {
        private PostExecution postExecution;
        public RefreshStatusTask(PostExecution postExecution) {
            this.postExecution = postExecution;
        }
        @Override
        protected Void doInBackground(String... uid) {
            User user = User.getInstance();
            statuses.clear();
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(postExecution.getActivity().getResources().getString(R.string.server_host) + "/post/" + user.getUid() + "/");
                HttpResponse response = client.execute(get);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseText = EntityUtils.toString(response.getEntity());
                    JSONArray results = new JSONArray(responseText);
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject obj = results.getJSONObject(i);
                        statuses.put(obj.getString("id"), new edu.usc.yuting.trojannow.status.abstraction.Status(obj.getString("id"), obj.getString("user"),obj.getString("uid"), obj.getString("text"), obj.getBoolean("anonymous"), obj.getString("timestamp")));
                    }
                }
            }catch(Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            postExecution.onPostExecution();
        }
    }

    private class DeleteStatusTask extends AsyncTask<String, Void, Void> {
        private PostExecution postExecution;
        public DeleteStatusTask(PostExecution postExecution) {
            this.postExecution = postExecution;
        }
        @Override
        protected Void doInBackground(String... pid) {
            User user = User.getInstance();
            try {
                HttpClient client = new DefaultHttpClient();
                HttpDelete delete = new HttpDelete(postExecution.getActivity().getResources().getString(R.string.server_host) + "/post/" + user.getUid() + "/" + pid[0] + "/");
                client.execute(delete);
                statuses.remove(pid[0]);
            }catch(Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            postExecution.onPostExecution();
        }
    }

    private class CreateStatusTask extends AsyncTask<Status, Void, Void> {
        private PostExecution postExecution;
        public CreateStatusTask(PostExecution postExecution) {
            this.postExecution = postExecution;
        }
        @Override
        protected Void doInBackground(edu.usc.yuting.trojannow.status.abstraction.Status... status) {
            User user = User.getInstance();
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(postExecution.getActivity().getResources().getString(R.string.server_host) + "/post/" + user.getUid() + "/");
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair("text", status[0].getText()));
                pairs.add(new BasicNameValuePair("anonymous", status[0].isAnonymous() + ""));
                post.setEntity(new UrlEncodedFormEntity(pairs));
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseText = EntityUtils.toString(response.getEntity());
                    JSONObject results = new JSONObject(responseText);
                    String sid = results.getString("post_id");
                    SensorRepository.getInstance().uploadLocalSensors(status[0], sid, false, postExecution);
                }
            }catch(Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            postExecution.onPostExecution();
        }
    }

    public static StatusRepository getInstance() {
        return instance;
    }

    public void createStatus(Status status, PostExecution intent) {
        new CreateStatusTask(intent).execute(status);
    }

    public void deleteStatus(String pid, PostExecution intent) {
        if (statuses.containsKey(pid)) {
            new DeleteStatusTask(intent).execute(pid);
        }
    }

    public Status getStatus(String sid) {
        return statuses.get(sid);
    }

    public List<Status> getAllStatuses() {
        List<Status> tmp = new LinkedList<Status>();
        for (Map.Entry<String, Status> entry : statuses.entrySet()) {
            tmp.add(entry.getValue());
        }
        Collections.sort(tmp);
        return Collections.unmodifiableList(tmp);
    }

    public void refreshStatuses(PostExecution ui) {
        new RefreshStatusTask(ui).execute();
    }

    public void clearAll() {
        statuses.clear();
    }
}
