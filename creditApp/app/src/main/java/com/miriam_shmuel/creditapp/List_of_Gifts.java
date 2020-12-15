package com.miriam_shmuel.creditapp;

import java.util.List;

public class List_of_Gifts {
    private List<Gift_Credit> listGift;
    private String type;

    public List_of_Gifts() {
        this.listGift = null;
        this.type = "Gifts";
    }

    public List<Gift_Credit> getlistGift() {
        return listGift;
    }

    public void setlistGift(List<Gift_Credit> listGift) {
        this.listGift = listGift;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
