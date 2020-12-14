package com.miriam_shmuel.creditapp;

import java.util.Date;
import java.util.List;

public class Waranty {
    private Shop shopName;
    private List<Item> items;
    private String receip;
    private int barCode;
    private Date changingDate;

    public Waranty(Shop shopName, List<Item> items, String receip, int barCode, Date changingDate) {
        this.shopName = shopName;
        this.items = items;
        this.receip = receip;
        this.barCode = barCode;
        this.changingDate = changingDate;
    }

    public Shop getShopName() {
        return shopName;
    }

    public void setShopName(Shop shopName) {
        this.shopName = shopName;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getReceip() {
        return receip;
    }

    public void setReceip(String receip) {
        this.receip = receip;
    }

    public int getBarCode() {
        return barCode;
    }

    public void setBarCode(int barCode) {
        this.barCode = barCode;
    }

    public Date getChangingDate() {
        return changingDate;
    }

    public void setChangingDate(Date changingDate) {
        this.changingDate = changingDate;
    }
}
