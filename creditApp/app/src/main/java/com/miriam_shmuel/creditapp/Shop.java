package com.miriam_shmuel.creditapp;

public class Shop {
    private String name;

    public Shop(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return name;
    }
}

