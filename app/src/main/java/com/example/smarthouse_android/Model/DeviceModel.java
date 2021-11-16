package com.example.smarthouse_android.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DeviceModel implements Serializable {

    @SerializedName("LightSwitch")
    @Expose
    private String LightSwitch;

    @SerializedName("DoorSwitch")
    @Expose
    private String DoorSwitch;

    @SerializedName("Humidity")
    @Expose
    private int Humidity;

    @SerializedName("Temperature")
    @Expose
    private int Temperature;

    @SerializedName("WindowSwitch")
    @Expose
    private String WindowSwitch;


    public String getLightSwitch() {
        return LightSwitch;
    }

    public void setLightSwitch(String lightSwitch) {
        LightSwitch = lightSwitch;
    }

    public String getDoorSwitch() {
        return DoorSwitch;
    }

    public void setDoorSwitch(String doorSwitch) {
        DoorSwitch = doorSwitch;
    }

    public int getHumidity() {
        return Humidity;
    }

    public void setHumidity(int humidity) {
        Humidity = humidity;
    }

    public int getTemperature() {
        return Temperature;
    }

    public void setTemperature(int temperature) {
        Temperature = temperature;
    }

    public String getWindowSwitch() {
        return WindowSwitch;
    }

    public void setWindowSwitch(String windowSwitch) {
        WindowSwitch = windowSwitch;
    }
}
