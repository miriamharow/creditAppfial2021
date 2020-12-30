package com.miriam_shmuel.creditapp;

import java.util.Date;
import java.util.List;

public class Gift_Credit {
    private String picture, type;
    private int barCode;
    private Date expirationDate;
    private List<Shop> shopName;
    private boolean used;

    public Gift_Credit(String picture, String type, int barCode, Date expirationDate, List<Shop> shopName) {
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

    public int getBarCode() {
        return barCode;
    }

    public void setBarCode(int barCode) {
        this.barCode = barCode;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public List<Shop> getShopName() {
        return shopName;
    }

    public void setShopName(List<Shop> shopName) {
        this.shopName = shopName;
    }




}
