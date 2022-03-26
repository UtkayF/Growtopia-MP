package com.warnstudio.growtopiamarketplace.Models;

public class Post {

    String date;
    String itemname;
    String postid;
    String price;
    String publisher;
    String worldname;
    String buyselldemo;
    String type;

    public Post() {
    }

    public Post(String date, String itemname, String postid, String price, String publisher, String worldname, String buyselldemo, String type) {
        this.date = date;
        this.itemname = itemname;
        this.postid = postid;
        this.price = price;
        this.publisher = publisher;
        this.worldname = worldname;
        this.buyselldemo = buyselldemo;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getWorldname() {
        return worldname;
    }

    public void setWorldname(String worldname) {
        this.worldname = worldname;
    }

    public String getBuyselldemo() {
        return buyselldemo;
    }

    public void setBuyselldemo(String buyselldemo) {
        this.buyselldemo = buyselldemo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
