package com.miriam_shmuel.creditapp;

import java.util.ArrayList;

public class Gift_Credit {
    private String type;
    private String key;
    private String barCode;
    private String value;
    private String expirationDate;
    private ArrayList<Shop> shopName;
    private boolean used;
    private String giftName;

    public Gift_Credit(String key, String barCode, String expirationDate, ArrayList<Shop> shopName, String type, String value, String giftName) {
        this.type = type;
        this.barCode = barCode;
        this.expirationDate = expirationDate;
        this.shopName = shopName;
        this.used = false;
        this.value = value;
        this.giftName = giftName;
        this.key = key;
    }

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
}
