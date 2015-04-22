package edu.usc.yuting.trojannow.message;

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

import edu.usc.yuting.trojannow.SendIntent;
import edu.usc.yuting.trojannow.UpdateUI;
import edu.usc.yuting.trojannow.login.User;

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
        private UpdateUI uiOperation;
        public RefreshMessageTask(UpdateUI uiOperation) {
            this.uiOperation = uiOperation;
        }
        @Override
        protected Void doInBackground(String... uid) {
            User user = User.getInstance();
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet("http://10.0.2.2:8000/message/" + user.getUid() + "/");
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
            uiOperation.onUpdateUI();
        }

    }

    private class UploadMessageTask extends AsyncTask<String, Void, Void> {
        private SendIntent intent;
        public UploadMessageTask(SendIntent intent) {
            this.intent = intent;
        }
        @Override
        protected Void doInBackground(String... uid) {
            User user = User.getInstance();
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://10.0.2.2:8000/message/" + uid[0] + "/" + user.getUid() + "/");
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
            intent.onSendIntent();
        }

    }

    public void refreshMessage(UpdateUI ui) {
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

    public void uploadMessage(String receiver, String text, SendIntent intent) {
        new UploadMessageTask(intent).execute(receiver, text);
    }
}
