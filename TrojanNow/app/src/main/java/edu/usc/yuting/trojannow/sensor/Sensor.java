package edu.usc.yuting.trojannow.sensor;

/**
 * Created by chengyey on 3/30/15.
 */
import java.io.Serializable;

import edu.usc.yuting.trojannow.status.Status;
public class Sensor implements Serializable{
    private String source;
    private String information;
    private String id;
    public Sensor(String source, String information) {
        /*
        Constructor for the wrapper of sensor data
         */
        this.source = source;
        this.information = information;
    }

    public Sensor(String id, String source, String information) {
        /*
        Constructor for the wrapper of sensor data
         */
        this.source = source;
        this.information = information;
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public String getInformation() {
        return  information;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
