package com.miriam_shmuel.creditapp;

import java.io.Serializable;

public class Warranty implements Serializable {
    private String shopName;
    private String itemName;
    private String barCode;
    private String expirationDate;
    private String key;
    private String pictureItemRecept;
    private String pictureShop;
    private String folder;

    public Warranty(String shopName, String barCode, String expirationDate, String itemName, String key, String pictureItem, String pictureShop, String folder) {
        this.shopName = shopName;
        this.barCode = barCode;
        this.expirationDate = expirationDate;
        this.itemName = itemName;
        this.key = key;
        this.pictureItemRecept = pictureItem;
        this.pictureShop = pictureShop;
        this.folder = folder;
    }

    public Warranty(){}

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
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

    public String getKey() { return key; }

    public void setKey(String key) { this.key = key; }

    public String getPictureItem() {
        return pictureItemRecept;
    }

    public void setPictureItem(String pictureItem) {
        this.pictureItemRecept = pictureItem;
    }

    public String getPictureShop() {
        return pictureShop;
    }

    public void setPictureShop(String pictureShop) {
        this.pictureShop = pictureShop;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }
}
