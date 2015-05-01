package edu.usc.yuting.trojannow.login.abstraction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.usc.yuting.trojannow.R;
import edu.usc.yuting.trojannow.common.PostExecution;
import edu.usc.yuting.trojannow.database.CommentRepository;
import edu.usc.yuting.trojannow.database.FriendRepository;
import edu.usc.yuting.trojannow.database.MessageRepository;
import edu.usc.yuting.trojannow.database.SensorRepository;
import edu.usc.yuting.trojannow.database.StatusRepository;
import edu.usc.yuting.trojannow.login.postexecution.MessageDialogPostExecution;

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
        private PostExecution postExecution;
        public LoginTask(PostExecution postExecution) {
            this.postExecution = postExecution;
        }
        @Override
        protected Void doInBackground(String... infos) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(postExecution.getActivity().getResources().getString(R.string.server_host) + "/login");
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
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (instance != null) {
                postExecution.onPostExecution();
            }
            else {
                ((EditText)postExecution.getActivity().findViewById(R.id.userNameText)).setText("");
                ((EditText)postExecution.getActivity().findViewById(R.id.passwordText)).setText("");
                new AlertDialog.Builder(postExecution.getActivity())
                        .setTitle("Login")
                        .setMessage("Login failed! ")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    }

    private static class CreateUserTask extends AsyncTask<String, Void, Void> {
        boolean successful = false;
        private PostExecution postExecution;
        public CreateUserTask(PostExecution postExecution) {
            this.postExecution = postExecution;
        }
        @Override
        protected Void doInBackground(String... infos) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(postExecution.getActivity().getResources().getString(R.string.server_host) + "/user/");
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
            ((MessageDialogPostExecution)postExecution).setTitle("Create User");
            ((MessageDialogPostExecution)postExecution).setContent(successful ? "User created successfully" : "User created failed");
            postExecution.onPostExecution();
        }
    }

    public static void authenticate(String userName, String password, PostExecution intent) {
        instance = null;
        new LoginTask(intent).execute(userName, password);
    }

    public static void createUser(String userName, String password, PostExecution dialog) {
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

    public void logout() {
        instance = null;
        CommentRepository.getInstance().clearAll();
        FriendRepository.getInstance().clearAll();
        MessageRepository.getInstance().clearAll();
        SensorRepository.getInstance().clearAll();
        StatusRepository.getInstance().clearAll();
    }

}
