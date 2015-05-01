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
import edu.usc.yuting.trojannow.comment.abstraction.Comment;
import edu.usc.yuting.trojannow.common.PostExecution;
import edu.usc.yuting.trojannow.login.abstraction.User;

/**
 * Created by Frank on 4/21/2015.
 */
public class CommentRepository {
    private Map<String, Comment> commentMap = new HashMap<String, Comment>();
    private static CommentRepository instance = new CommentRepository();

    private class RefreshCommentsTask extends AsyncTask<String, Void, Void> {
        private PostExecution postExecution;
        public RefreshCommentsTask(PostExecution postExecution) {
            this.postExecution = postExecution;
        }
        @Override
        protected Void doInBackground(String... pid) {
            commentMap.clear();
            try {
                User currentUser = User.getInstance();
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(postExecution.getActivity().getResources().getString(R.string.server_host) + "/comment/" + pid[0] + "/" + currentUser.getUid() + "/");
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
            postExecution.onPostExecution();
        }
    }

    private class CreateCommentTask extends AsyncTask<String, Void, Void> {
        private PostExecution postExecution;
        public CreateCommentTask(PostExecution postExecution) {
            this.postExecution = postExecution;
        }
        @Override
        protected Void doInBackground(String... pid) {
            User user = User.getInstance();
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(postExecution.getActivity().getResources().getString(R.string.server_host) + "/comment/" + pid[0] + "/" + user.getUid() + "/");
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair("text", pid[1]));
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

    private class DeleteCommentTask extends AsyncTask<String, Void, Void> {
        private PostExecution postExecution;
        public DeleteCommentTask(PostExecution postExecution) {
            this.postExecution = postExecution;
        }
        @Override
        protected Void doInBackground(String... cid) {
            User user = User.getInstance();
            try {
                HttpClient client = new DefaultHttpClient();
                HttpDelete delete = new HttpDelete(postExecution.getActivity().getResources().getString(R.string.server_host) + "/comment/" + cid[0] + "/");
                client.execute(delete);
                commentMap.remove(cid[0]);
            }catch(Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            postExecution.onPostExecution();
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

    public void refreshComments(String sid, PostExecution ui) {
        new RefreshCommentsTask(ui).execute(sid);
    }

    public void createComment(String sid, String text, PostExecution intent) {
        new CreateCommentTask(intent).execute(sid, text);
    }

    public void deleteComment(String cid, PostExecution ui) {
        if (commentMap.containsKey(cid)) {
            new DeleteCommentTask(ui).execute(cid);
        }
    }

    public void clearAll() {
        commentMap.clear();
    }

}
