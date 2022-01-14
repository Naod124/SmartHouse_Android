package com.example.smarthouse_android.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DeviceModel implements Serializable {

    @SerializedName("lights")
    @Expose
    private Light[] LightSwitch;

    @SerializedName("doors")
    @Expose
    private Door[] DoorSwitch;

    @SerializedName("humidity")
    @Expose
    private int humidity;

    @SerializedName("temperature")
    @Expose
    private int temperature;

    @SerializedName("windows")
    @Expose
    private Window[] WindowSwitch;





    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }


    public Light[] getLightSwitch() {
        return LightSwitch;
    }

    public void setLightSwitch(Light[] lightSwitch) {
        LightSwitch = lightSwitch;
    }

    public Door[] getDoorSwitch() {
        return DoorSwitch;
    }

    public void setDoorSwitch(Door[] doorSwitch) {
        DoorSwitch = doorSwitch;
    }

    public Window[] getWindowSwitch() {
        return WindowSwitch;
    }

    public void setWindowSwitch(Window[] windowSwitch) {
        WindowSwitch = windowSwitch;
    }
}
