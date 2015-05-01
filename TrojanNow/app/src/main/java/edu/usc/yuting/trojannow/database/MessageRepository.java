package edu.usc.yuting.trojannow.database;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
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
import edu.usc.yuting.trojannow.message.abstraction.Message;

/**
 * Created by Frank on 4/21/2015.
 */
public class MessageRepository {
    private Map<String, Message> messageMap = new HashMap<String, Message>();
    private MessageRepository() {

    }
    private static MessageRepository instance = new MessageRepository();

    public static MessageRepository getInstance() {
        return instance;
    }

    private class RefreshMessageTask extends AsyncTask<String, Void, Void> {
        private PostExecution postExecution;
        public RefreshMessageTask(PostExecution postExecution) {
            this.postExecution = postExecution;
        }
        @Override
        protected Void doInBackground(String... uid) {
            User user = User.getInstance();
            messageMap.clear();
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(postExecution.getActivity().getResources().getString(R.string.server_host) + "/message/" + user.getUid() + "/");
                HttpResponse response = client.execute(get);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseText = EntityUtils.toString(response.getEntity());
                    JSONArray results = new JSONArray(responseText);
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject obj = results.getJSONObject(i);
                        messageMap.put(obj.getString("id"), new Message(obj.getString("id"), obj.getString("sender_id"),obj.getString("sender_name"), obj.getString("receiver_id"), obj.getString("receiver_name"), obj.getString("text"), obj.getString("timestamp")));
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

    private class UploadMessageTask extends AsyncTask<String, Void, Void> {
        private PostExecution postExecution;
        public UploadMessageTask(PostExecution postExecution) {
            this.postExecution = postExecution;
        }
        @Override
        protected Void doInBackground(String... uid) {
            User user = User.getInstance();
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(postExecution.getActivity().getResources().getString(R.string.server_host) + "/message/" + uid[0] + "/" + user.getUid() + "/");
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair("text", uid[1]));
                post.setEntity(new UrlEncodedFormEntity(pairs));
                client.execute(post);
            }catch(Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            postExecution.onPostExecution();
        }

    }

    public void refreshMessage(PostExecution ui) {
        new RefreshMessageTask(ui).execute();
    }

    public List<Message> getMessages() {
        List<Message> messages = new LinkedList<Message>();
        for (Map.Entry<String, Message> entry : messageMap.entrySet()) {
            if (entry.getValue().getSenderId().equals(User.getInstance().getUid()) || entry.getValue().getReceiverId().equals(User.getInstance().getUid())) {
                messages.add(entry.getValue());
            }
        }
        Collections.sort(messages);
        return Collections.unmodifiableList(messages);
    }

    public void uploadMessage(String receiver, String text, PostExecution intent) {
        new UploadMessageTask(intent).execute(receiver, text);
    }

    public void clearAll() {
        messageMap.clear();
    }
}
