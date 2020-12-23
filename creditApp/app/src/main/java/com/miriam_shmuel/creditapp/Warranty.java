package com.miriam_shmuel.creditapp;

import java.util.Date;

public class Warranty {
    private Shop shopName;
    private String itemName;
    private String itemReceipt;
    private String shopReceipt;
    private int barCode;
    private Date expirationDate;

    public Warranty(Shop shopName, int barCode, Date expirationDate, String itemName, String itemReceipt, String shopReceipt) {
        this.shopName = shopName;
        this.barCode = barCode;
        this.expirationDate = expirationDate;
        this.itemName = itemName;
        this.itemReceipt = itemReceipt;
        this.shopReceipt = shopReceipt;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemReceipt() {
        return itemReceipt;
    }

    public void setItemReceipt(String itemReceipt) {
        this.itemReceipt = itemReceipt;
    }

    public Shop getShopName() {
        return shopName;
    }

    public void setShopName(Shop shopName) {
        this.shopName = shopName;
    }

    public String getShopReceipt() {
        return shopReceipt;
    }

    public void setShopReceipt(String shopReceipt) {
        this.shopReceipt = shopReceipt;
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
}
