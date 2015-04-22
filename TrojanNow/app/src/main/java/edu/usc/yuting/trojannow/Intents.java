package edu.usc.yuting.trojannow;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Frank on 4/18/2015.
 */
public class Intents {
    public static Map<String, String> intents = new HashMap<String, String>();
    static {
        intents.put("DASHBOARD_INTENT", "edu.usc.yuting.trojannow.intent.dashboard");
        intents.put("STATUS_INTENT_ID", "edu.usc.yuting.trojannow.intent.status.id");
        intents.put("STATUS_INTENT", "edu.usc.yuting.trojannow.intent.status");
        intents.put("SENSOR_INTENT", "edu.usc.yuting.trojannow.intent.sensor.refresh");
    }
}
