package com.miriam_shmuel.creditapp;


import java.util.List;

public class User {

    private String name, email;
    private List<Waranty> listWaranties;
    private List_of_Credits listcredit; //new , get, set
    private List_of_Gifts listGift; //new , get , set

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.listWaranties = null;
        this.listcredit = null;
        this.listGift = null;
    }



}
