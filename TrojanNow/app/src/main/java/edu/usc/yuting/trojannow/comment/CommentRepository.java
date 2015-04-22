package edu.usc.yuting.trojannow.comment;

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

import edu.usc.yuting.trojannow.common.SendIntent;
import edu.usc.yuting.trojannow.common.UpdateUI;
import edu.usc.yuting.trojannow.login.User;

/**
 * Created by Frank on 4/21/2015.
 */
public class CommentRepository {
    private Map<String, Comment> commentMap = new HashMap<String, Comment>();
    private static CommentRepository instance = new CommentRepository();

    private class RefreshCommentsTask extends AsyncTask<String, Void, Void> {
        private UpdateUI uiOperation;
        public RefreshCommentsTask(UpdateUI uiOperation) {
            this.uiOperation = uiOperation;
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
                        commentMap.put(obj.getString("id"), new Comment(obj.getString("id"),obj.getString("post_id"), obj.getString("user_id"), obj.getString("user"), obj.getString("text"), obj.getString("timestamp")));
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

    private class CreateCommentTask extends AsyncTask<String, Void, Void> {
        private SendIntent intent;
        public CreateCommentTask(SendIntent intent) {
            this.intent = intent;
        }
        @Override
        protected Void doInBackground(String... pid) {
            User user = User.getInstance();
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://10.0.2.2:8000/comment/" + pid[0] + "/" + user.getUid() + "/");
                List<NameValuePair> paris = new ArrayList<NameValuePair>();
                paris.add(new BasicNameValuePair("text", pid[1]));
                post.setEntity(new UrlEncodedFormEntity(paris));
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

    private class DeleteCommentTask extends AsyncTask<String, Void, Void> {
        private UpdateUI uiOperation;
        public DeleteCommentTask(UpdateUI uiOperation) {
            this.uiOperation = uiOperation;
        }
        @Override
        protected Void doInBackground(String... cid) {
            User user = User.getInstance();
            try {
                HttpClient client = new DefaultHttpClient();
                HttpDelete delete = new HttpDelete("http://10.0.2.2:8000/comment/" + cid[0] + "/");
                client.execute(delete);
                commentMap.remove(cid[0]);
            }catch(Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            uiOperation.onUpdateUI();
        }
    }

    private CommentRepository() {

    }

    public static CommentRepository getInstance() {
        return instance;
    }

    public List<Comment> getCommentsFromStatusId(String sid) {
        List<Comment> comments = new LinkedList<Comment>();
        for (Map.Entry<String, Comment> entry : commentMap.entrySet()) {
            if (entry.getValue().getStatusId().equals(sid)) {
                comments.add(entry.getValue());
            }
        }
        Collections.sort(comments);
        return Collections.unmodifiableList(comments);
    }

    public void refreshComments(String sid, UpdateUI ui) {
        new RefreshCommentsTask(ui).execute(sid);
    }

    public void createComment(String sid, String text, SendIntent intent) {
        new CreateCommentTask(intent).execute(sid, text);
    }

    public void deleteComment(String cid, UpdateUI ui) {
        if (commentMap.containsKey(cid)) {
            new DeleteCommentTask(ui).execute(cid);
        }
    }
}
