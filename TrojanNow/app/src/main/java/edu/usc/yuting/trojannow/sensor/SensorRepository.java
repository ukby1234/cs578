package edu.usc.yuting.trojannow.sensor;

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

import edu.usc.yuting.trojannow.UpdateUI;
import edu.usc.yuting.trojannow.status.Status;

/**
 * Created by Frank on 4/21/2015.
 */
public class SensorRepository {
    private Map<String, Sensor> serverSensors = new HashMap<String, Sensor>();
    private Map<Status, List<Sensor>> localSensors = new HashMap<Status, List<Sensor>>();
    private static SensorRepository instance = new SensorRepository();
    private SensorRepository(){

    }
    private class RefreshSensorsTask extends AsyncTask<String, Void, Void> {
        private UpdateUI uiOperation;
        public RefreshSensorsTask(UpdateUI uiOperation) {
            this.uiOperation = uiOperation;
        }
        @Override
        protected Void doInBackground(String... pid) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet("http://10.0.2.2:8000/attribute/" + pid[0] + "/");
                HttpResponse response = client.execute(get);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseText = EntityUtils.toString(response.getEntity());
                    JSONArray results = new JSONArray(responseText);
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject obj = results.getJSONObject(i);
                        serverSensors.put(obj.getString("id"), new Sensor(obj.getString("id"), obj.getString("post_id"), obj.getString("source"), obj.getString("information")));
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

    private class CreateSensorsTask extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... status) {
            try {
                HttpClient client = new DefaultHttpClient();
                for (Sensor sensor : localSensors.get(status[0])) {
                    HttpPost sensorPost = new HttpPost("http://10.0.2.2:8000/attribute/" + status[1] + "/environment/");
                    List<NameValuePair> sensorPairs = new ArrayList<NameValuePair>();
                    sensorPairs.add(new BasicNameValuePair("source", sensor.getSource()));
                    sensorPairs.add(new BasicNameValuePair("information", sensor.getInformation()));
                    sensorPost.setEntity(new UrlEncodedFormEntity(sensorPairs));
                    client.execute(sensorPost).getEntity();
                }
                localSensors.remove(status[0]);
            }catch(Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

    public static SensorRepository getInstance() {
        return instance;
    }

    public List<Sensor> getSensorsFromStatusId(String sid) {
        List<Sensor> sensors = new LinkedList<Sensor>();
        for (Map.Entry<String, Sensor> entry : serverSensors.entrySet()) {
            if (entry.getValue().getStatusId().equals(sid)) {
                sensors.add(entry.getValue());
            }
        }
        return Collections.unmodifiableList(sensors);
    }


    public void refreshSensors(String sid, UpdateUI ui) {
        new RefreshSensorsTask(ui).execute(sid);
    }

    public void addLocalSensor(Status status, Sensor localSensor) {
        List<Sensor> sensors = localSensors.get(status);
        if (sensors == null) {
            sensors = new LinkedList<Sensor>();
        }
        sensors.add(localSensor);
        localSensors.put(status, sensors);
    }

    public List<Sensor> getLocalSensors(Status status) {
        List<Sensor> sensors = new LinkedList<Sensor>();
        if (status != null) {
            List<Sensor> t = localSensors.get(status);
            sensors.addAll(t);
        }
        return Collections.unmodifiableList(sensors);
    }

    public void uploadLocalSensors(Status status, String sid, boolean async) {
        List<Sensor> sensors = localSensors.get(status);
        if (sensors != null) {
            CreateSensorsTask task = new CreateSensorsTask();
            if (!async) {
                task.doInBackground(status, sid);
            }
            else {
                task.execute(status, sid);
            }
        }
    }

    public void removeSensor(Status status, Sensor sensor) {
        List<Sensor> sensors = localSensors.get(status);
        if (sensors != null) {
            sensors.remove(sensor);
        }
    }
}
