package edu.usc.yuting.trojannow.login;

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
import java.util.List;

/**
 * Created by chengyey on 3/30/15.
 */
public class User implements Serializable{
    private String uid;
    private String userName;
    private static User instance = null;
    private User(String uid, String userName) {
        this.uid = uid;
        this.userName = userName;
    }

    public static User authenticate(String userName, String password) {
        instance = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://10.0.2.2:8000/login");
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("username", userName));
            pairs.add(new BasicNameValuePair("passwd", password));
            post.setEntity(new UrlEncodedFormEntity(pairs));
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                String responseText = EntityUtils.toString(response.getEntity());
                JSONObject res = new JSONObject(responseText);
                instance = new User(res.getString("uid"), res.getString("username"));
            }
        }catch(Exception e) {

        }
        return instance;
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
}
