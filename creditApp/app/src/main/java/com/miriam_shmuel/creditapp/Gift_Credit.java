package com.miriam_shmuel.creditapp;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;

public class Gift_Credit implements Serializable {
    private String type;
    private String key;
    private String barCode;
    private String value;
    private String expirationDate;
    private ArrayList<Shop> shopName;
    private boolean used;
    private String giftName;
    private String picture;
    private int notificationID;

    public Gift_Credit(String key, String barCode, String expirationDate, ArrayList<Shop> shopName, String type, String value, String giftName, String picture) {
        this.type = type;
        this.barCode = barCode;
        this.expirationDate = expirationDate;
        this.shopName = shopName;
        this.used = false;
        this.value = value;
        this.giftName = giftName;
        this.key = key;
        this.picture = picture;
        this.notificationID = (int) Timestamp.now().getSeconds();;
    }

    public Gift_Credit(){}

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() { return type; }

    public void setType(String type) {
        this.type = type;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public ArrayList<Shop> getShopName() {
        return shopName;
    }

    public void setShopName(ArrayList<Shop> shopName) {
        this.shopName = shopName;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

}
