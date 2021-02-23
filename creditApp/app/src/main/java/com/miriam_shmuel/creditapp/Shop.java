package com.miriam_shmuel.creditapp;

import java.io.Serializable;

public class Shop implements Serializable {
    private String name;

    public Shop(String name){
        this.name = name;
    }

    public Shop(){}

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

