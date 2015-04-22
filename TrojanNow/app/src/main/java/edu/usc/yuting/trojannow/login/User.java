package edu.usc.yuting.trojannow.login;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.usc.yuting.trojannow.common.CreateDialog;
import edu.usc.yuting.trojannow.common.SendIntent;
import edu.usc.yuting.trojannow.common.UpdateUI;

/**
 * Created by chengyey on 3/30/15.
 */
public class User implements Serializable{
    private String uid;
    private String userName;
    private static User instance = null;
    private Map<String, User> userMap = new HashMap<String, User>();
    private User(String uid, String userName) {
        this.uid = uid;
        this.userName = userName;
    }

    private static class LoginTask extends AsyncTask<String, Void, Void> {
        private SendIntent intent;
        public LoginTask(SendIntent intent) {
            this.intent = intent;
        }
        @Override
        protected Void doInBackground(String... infos) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://10.0.2.2:8000/login");
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair("username", infos[0]));
                pairs.add(new BasicNameValuePair("passwd", infos[1]));
                post.setEntity(new UrlEncodedFormEntity(pairs));
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseText = EntityUtils.toString(response.getEntity());
                    JSONObject res = new JSONObject(responseText);
                    instance = new User(res.getString("uid"), res.getString("username"));
                }
            }catch(Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (instance != null) {
                intent.onSendIntent();
            }
        }
    }

    private static class CreateUserTask extends AsyncTask<String, Void, Void> {
        private CreateDialog dialogOperation;
        boolean successful = false;
        public CreateUserTask(CreateDialog dialogOperation) {
            this.dialogOperation = dialogOperation;
        }
        @Override
        protected Void doInBackground(String... infos) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://10.0.2.2:8000/user/");
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair("username", infos[0]));
                pairs.add(new BasicNameValuePair("passwd", infos[1]));
                post.setEntity(new UrlEncodedFormEntity(pairs));
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200) {
                    successful = true;
                }
            }catch(Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dialogOperation.onCreateDialog("Create User", successful ? "User created successfully" : "User created failed");
        }
    }

    private class GetUsersTask extends AsyncTask<String, Void, Void> {
        private UpdateUI uiOperation;
        public GetUsersTask(UpdateUI uiOperation) {
            this.uiOperation = uiOperation;
        }
        @Override
        protected Void doInBackground(String... infos) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet("http://10.0.2.2:8000/user/");
                HttpResponse response = client.execute(get);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseText = EntityUtils.toString(response.getEntity());
                    JSONArray results = new JSONArray(responseText);
                    for (int i = 0; i <results.length(); i++) {
                        JSONObject obj = results.getJSONObject(i);
                        userMap.put(obj.getString("id"), new User(obj.getString("id"), obj.getString("user_name")));
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

    public static void authenticate(String userName, String password, SendIntent intent) {
        instance = null;
        new LoginTask(intent).execute(userName, password);
    }

    public static void createUser(String userName, String password, CreateDialog dialog) {
        instance = null;
        new CreateUserTask(dialog).execute(userName, password);
    }

    public static User getInstance() {
        return instance;
    }

    public String getUid() {
        return uid;
    }

    public String getUserName() {
        return userName;
    }

    public void refreshUser(UpdateUI ui) {
        new GetUsersTask(ui).execute();
    }

    public List<User> getUsers() {
        List<User> users = new LinkedList<User>();
        for (Map.Entry<String, User> entry : userMap.entrySet()) {
            if (!entry.getKey().equals(uid)) {
                users.add(entry.getValue());
            }
        }
        return Collections.unmodifiableList(users);
    }

    public String getId(String userName) {
        for (Map.Entry<String, User> entry : userMap.entrySet()) {
            if (entry.getValue().userName.equals(userName)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
