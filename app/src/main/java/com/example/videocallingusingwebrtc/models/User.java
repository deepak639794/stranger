package com.example.videocallingusingwebrtc.models;

public class User {
    private String uId,name, city,profile;

    public User(){

    }

    public User(String uId, String name, String city, String profile) {
        this.uId = uId;
        this.name = name;
        this.city = city;
        this.profile = profile;
    }

    public String getuId() {
        return uId;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getProfile() {
        return profile;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}

