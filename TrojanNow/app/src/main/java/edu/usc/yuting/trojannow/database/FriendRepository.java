package edu.usc.yuting.trojannow.database;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.usc.yuting.trojannow.R;
import edu.usc.yuting.trojannow.common.PostExecution;
import edu.usc.yuting.trojannow.login.abstraction.User;
import edu.usc.yuting.trojannow.usermgmt.abstraction.FriendCandidate;
import edu.usc.yuting.trojannow.usermgmt.abstraction.FriendUser;

/**
 * Created by chengyey on 4/30/15.
 */
public class FriendRepository {
    private static FriendRepository instance = new FriendRepository();
    Map<String, FriendUser> friendsMap = new HashMap<>();
    Map<String, FriendCandidate> candidatesMap = new HashMap<>();
    public static FriendRepository getInstance() {
        return instance;
    }
    private FriendRepository() {

    }
    private class GetFriendsTask extends AsyncTask<Void, Void, Void> {
        private PostExecution postExecution;
        public GetFriendsTask(PostExecution postExecution) {
            this.postExecution = postExecution;
        }
        @Override
        protected Void doInBackground(Void... pid) {
            try {
                HttpClient client = new DefaultHttpClient();
                User currentUser = User.getInstance();
                HttpGet get = new HttpGet(postExecution.getActivity().getResources().getString(R.string.server_host) + "/friend/" + currentUser.getUid() + "/");
                HttpResponse response = client.execute(get);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseText = EntityUtils.toString(response.getEntity());
                    JSONArray results = new JSONArray(responseText);
                    friendsMap.clear();
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject obj = results.getJSONObject(i);
                        friendsMap.put(obj.getString("friend_id"), new FriendUser(obj.getString("friend_id"),obj.getString("friend_username"), obj.getBoolean("accepted"), obj.getBoolean("direction")));
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

    private class ApproveFriendTask extends AsyncTask<String, Void, Void> {
        private PostExecution postExecution;
        public ApproveFriendTask(PostExecution postExecution) {
            this.postExecution = postExecution;
        }
        @Override
        protected Void doInBackground(String... uid) {
            try {
                HttpClient client = new DefaultHttpClient();
                User currentUser = User.getInstance();
                HttpPut put = new HttpPut(postExecution.getActivity().getResources().getString(R.string.server_host) + "/friend/" + currentUser.getUid() + "/" + uid[0] + "/");
                HttpResponse response = client.execute(put);
                if (response.getStatusLine().getStatusCode() == 200) {
                    FriendUser friend = friendsMap.get(uid[0]);
                    friend.setAccepted(true);
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

    private class DeleteFriendTask extends AsyncTask<String, Void, Void> {
        private PostExecution postExecution;
        public DeleteFriendTask(PostExecution postExecution) {
            this.postExecution = postExecution;
        }
        @Override
        protected Void doInBackground(String... uid) {
            try {
                HttpClient client = new DefaultHttpClient();
                User currentUser = User.getInstance();
                HttpDelete delete = new HttpDelete(postExecution.getActivity().getResources().getString(R.string.server_host) + "/friend/" + currentUser.getUid() + "/" + uid[0] + "/");
                HttpResponse response = client.execute(delete);
                if (response.getStatusLine().getStatusCode() == 200) {
                    friendsMap.remove(uid[0]);
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

    private class AddFriendTask extends AsyncTask<String, Void, Void> {
        private PostExecution postExecution;
        public AddFriendTask(PostExecution postExecution) {
            this.postExecution = postExecution;
        }
        @Override
        protected Void doInBackground(String... uid) {
            try {
                HttpClient client = new DefaultHttpClient();
                User currentUser = User.getInstance();
                HttpPost post = new HttpPost(postExecution.getActivity().getResources().getString(R.string.server_host) + "/friend/" + currentUser.getUid() + "/" + uid[0] + "/");
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200) {
                    candidatesMap.remove(uid[0]);
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

    private class GetCandidatesTask extends AsyncTask<Void, Void, Void> {
        private PostExecution postExecution;
        public GetCandidatesTask(PostExecution postExecution) {
            this.postExecution = postExecution;
        }
        @Override
        protected Void doInBackground(Void... pid) {
            try {
                HttpClient client = new DefaultHttpClient();
                User currentUser = User.getInstance();
                HttpGet get = new HttpGet(postExecution.getActivity().getResources().getString(R.string.server_host) + "/friend/candidate/" + currentUser.getUid() + "/");
                HttpResponse response = client.execute(get);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseText = EntityUtils.toString(response.getEntity());
                    JSONArray results = new JSONArray(responseText);
                    candidatesMap.clear();
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject obj = results.getJSONObject(i);
                        candidatesMap.put(obj.getString("id"), new FriendCandidate(obj.getString("id"),obj.getString("username")));
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

    public void updateFriendList(PostExecution pe) {
        new GetFriendsTask(pe).execute();
    }

    public void approveFriend(String friendId, PostExecution pe) {
        new ApproveFriendTask(pe).execute(friendId);
    }

    public void deleteFriend(String friendId, PostExecution pe) {
        new DeleteFriendTask(pe).execute(friendId);
    }

    public void addFriend(String friendId, PostExecution pe) {
        new AddFriendTask(pe).execute(friendId);
    }

    public List<FriendUser> getFriends() {
        List<FriendUser> users = new LinkedList<>();
        for (Map.Entry<String, FriendUser> entry : friendsMap.entrySet()) {
            users.add(entry.getValue());
        }
        return Collections.unmodifiableList(users);
    }

    public String getId(String userName) {
        for (Map.Entry<String, FriendUser> entry : friendsMap.entrySet()) {
            if (entry.getValue().getUserName().equals(userName)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void refreshCandidates(PostExecution pe) {
        new GetCandidatesTask(pe).execute();
    }

    public List<FriendCandidate> getCandidates() {
        List<FriendCandidate> candidates = new LinkedList<>();
        for (Map.Entry<String, FriendCandidate> entry : candidatesMap.entrySet()) {
            candidates.add(entry.getValue());
        }
        return Collections.unmodifiableList(candidates);
    }

    public void clearAll() {
        friendsMap.clear();
        candidatesMap.clear();
    }
}
