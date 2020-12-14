package com.miriam_shmuel.creditapp;

import java.util.List;

public class List_of_Gifts {
    private List<Gift_Credit> lg;
    private String type;

    public List<Gift_Credit> getLg() {
        return lg;
    }

    public void setLg(List<Gift_Credit> lg) {
        this.lg = lg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List_of_Gifts() {
        this.lg = null;
        this.type = "Gifts";
    }
}
