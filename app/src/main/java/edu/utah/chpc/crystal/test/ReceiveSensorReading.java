package edu.utah.chpc.crystal.test;

/**
 * Created by crystal on 3/9/2018.
 */


public class ReceiveSensorReading {
    String sensorReading = "";
    String sensorRequest = "";

    public String getSensorRequest(){

        return sensorRequest;
    }

    public void setSensorRequest(String newSensorRequest){
        this.sensorRequest = newSensorRequest;
    }

}

