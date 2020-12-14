package com.miriam_shmuel.creditapp;


import java.util.List;

public class User {

    private String name, email;
    private List<Waranty> listWaranties;


    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.listWaranties = null;


    }



}
