package com.miriam_shmuel.creditapp;

public class Shop {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public Shop(String name){
        this.name = name;
    }
}
