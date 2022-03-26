package com.warnstudio.growtopiamarketplace.Models;

public class User {

    private String id;
    private String imageurl;
    private String instagram;
    private String mainworld;
    private String point;
    private String telegram;
    private String username;
    private String admin;

    public User() {
    }

    public User(String id, String imageurl, String instagram, String mainworld, String point, String telegram, String username, String admin) {
        this.id = id;
        this.imageurl = imageurl;
        this.instagram = instagram;
        this.mainworld = mainworld;
        this.point = point;
        this.telegram = telegram;
        this.username = username;
        this.admin = admin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getMainworld() {
        return mainworld;
    }

    public void setMainworld(String mainworld) {
        this.mainworld = mainworld;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getTelegram() {
        return telegram;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }
}
