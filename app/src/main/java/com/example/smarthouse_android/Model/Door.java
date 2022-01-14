package com.example.smarthouse_android.Model;

import java.io.Serializable;

public class Door implements Serializable {
    private int doorId;
    private boolean doorStatus;

    public Door(int doorId, boolean doorStatus) {
        this.doorId = doorId;
        this.doorStatus = doorStatus;
    }

    public Door() {
    }

    public int getDoorId() {
        return doorId;
    }

    public void setDoorId(int doorId) {
        this.doorId = doorId;
    }

    public boolean isDoorStatus() {
        return doorStatus;
    }

    public void setDoorStatus(boolean doorStatus) {
        this.doorStatus = doorStatus;
    }

    @Override
    public String toString() {
        return "Door{" +
                "doorId=" + doorId +
                ", doorStatus=" + doorStatus +
                '}';
    }
}
