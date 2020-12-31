package com.miriam_shmuel.creditapp;

import java.util.ArrayList;

public class Gift_Credit {
    private String picture, type;
    private String barCode;
    private String expirationDate;
    private ArrayList<Shop> shopName;



    private boolean used;

    public Gift_Credit(String picture, String type, String barCode, String expirationDate, ArrayList<Shop> shopName) {
        this.picture = picture;
        this.type = type;
        this.barCode = barCode;
        this.expirationDate = expirationDate;
        this.shopName = shopName;
        this.used = false;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getType() {
        return type;
    }

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


}
