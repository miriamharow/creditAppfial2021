package com.miriam_shmuel.creditapp;

public class Warranty {
    private String shopName;
    private String itemName;
    private String itemReceipt;
    private String shopReceipt;
    private String barCode;
    private String expirationDate;

    public Warranty(String shopName, String barCode, String expirationDate, String itemName, String itemReceipt, String shopReceipt) {
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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopReceipt() {
        return shopReceipt;
    }

    public void setShopReceipt(String shopReceipt) {
        this.shopReceipt = shopReceipt;
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
}
