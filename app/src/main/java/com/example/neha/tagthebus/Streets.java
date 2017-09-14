package com.example.neha.tagthebus;

/**
 * Created by NEHA on 5/6/2017.
 */
//class street which has the function to set and get the details of a street
public class Streets {
    private int id;
    private String name;
    private String title;
    private byte[] image;
    private String time;
    private String user;

    public Streets(String name, String title, byte[] image, String time, String user, int id) {
        this.name = name;
        this.title = title;
        this.image = image;
        this.id = id;
        this.time = time;
        this.user = user;
    }

    public Streets(String title, byte[] image, String time, int id) {
        this.title = title;
        this.image = image;
        this.id = id;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String price) {
        this.title = title;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}
