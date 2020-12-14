package com.miriam_shmuel.creditapp;

import java.util.List;

public class List_of_Credits {
    private List<Gift_Credit> lc;
    private String type;

    public List<Gift_Credit> getLc() {
        return lc;
    }

    public void setLc(List<Gift_Credit> lc) {
        this.lc = lc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List_of_Credits() {
        this.lc = null;
        this.type = "Credits";
    }
}
