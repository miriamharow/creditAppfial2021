package com.miriam_shmuel.creditapp;

import java.util.Date;

public class Item {
    private String name;
    private String warantyPic;
    private Date expirationDate;

    public Item(String name, String warantyPic, Date expirationDate) {
        this.name = name;
        this.warantyPic = warantyPic;
        this.expirationDate = expirationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWarantyPic() {
        return warantyPic;
    }

    public void setWarantyPic(String warantyPic) {
        this.warantyPic = warantyPic;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
