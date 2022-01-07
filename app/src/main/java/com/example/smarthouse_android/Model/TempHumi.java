package com.example.smarthouse_android.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TempHumi implements Serializable {

    @SerializedName("temperature")
    @Expose
    private int temperature;

    @SerializedName("humidity")
    @Expose
    private int humidity;


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
}
