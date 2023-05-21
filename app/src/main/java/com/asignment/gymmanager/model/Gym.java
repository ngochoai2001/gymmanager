package com.asignment.gymmanager.model;

import lombok.Data;

@Data
public class Gym {
    private float x;
    private float y;
    private String name;
    private String address;
    private String time;

    public Gym() {
    }

    public Gym(float x, float y, String name, String address, String time) {

        this.x = x;
        this.y = y;
        this.name = name;
        this.address = address;
        this.time = time;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
