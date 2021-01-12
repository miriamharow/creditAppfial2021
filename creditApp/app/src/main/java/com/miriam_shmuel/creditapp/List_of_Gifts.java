package com.miriam_shmuel.creditapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class List_of_Gifts {
    private ArrayList<Gift_Credit> listOfGifts;
    private String type;
    private Map<String, Object> data;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String email, doc;

    public List_of_Gifts() {
        this.listOfGifts = new ArrayList<Gift_Credit>();
        this.data = new HashMap<>();
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
        doc = "list of gift";
    }

    public ArrayList<Gift_Credit> getlistGift() {
        return listOfGifts;
    }

    public void setlistGift(ArrayList<Gift_Credit> listGift) {
        this.listOfGifts = listGift;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String addGift(String picture, String barCode, String expirationDate, ArrayList<Shop> shopName) {
        String str = "";
        for (int i = 0; i<shopName.size(); i++)
                str += shopName.get(i).toString().replaceAll(" ","_");
        String key = str+barCode;
        Log.d("Debug", "OK" );
        //if(iSExist(key) == false){
        Gift_Credit gift = new Gift_Credit(key, barCode, expirationDate, shopName, "gift");
        this.listOfGifts.add(gift);

        data.put("gift", gift);

        db.collection(email).document(doc).collection(key)
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        //}
        return key;
    }
}
