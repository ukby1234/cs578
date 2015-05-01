package edu.usc.yuting.trojannow.sensor.abstraction;

/**
 * Created by chengyey on 3/30/15.
 */
import java.io.Serializable;

public class Sensor implements Serializable{
    private String source;
    private String information;
    private String id;
    private String statusId;
    public Sensor(String source, String information) {
        /*
        Constructor for the wrapper of sensor data
         */
        this.source = source;
        this.information = information;
    }

    public Sensor(String id, String statusId, String source, String information) {
        /*
        Constructor for the wrapper of sensor data
         */
        this.source = source;
        this.information = information;
        this.id = id;
        this.statusId = statusId;
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

    public String getStatusId() {
        return statusId;
    }

    public void setId(String id) {
        this.id = id;
    }
}
