package com.example.smarthouse_android.Model;

import java.io.Serializable;

public class Light implements Serializable {

    private int lightId ;
    private boolean lightStatus;

    public Light() {
    }

    public Light(int lightId, boolean lightStatus) {
        this.lightId = lightId;
        this.lightStatus = lightStatus;
    }


    public int getLightId() {
        return lightId;
    }

    public void setLightId(int lightId) {
        this.lightId = lightId;
    }

    public boolean isLightStatus() {
        return lightStatus;
    }

    public void setLightStatus(boolean lightStatus) {
        this.lightStatus = lightStatus;
    }

    @Override
    public String toString() {
        return "Light{" +
                "lightId=" + lightId +
                ", lightStatus=" + lightStatus +
                '}';
    }
}
