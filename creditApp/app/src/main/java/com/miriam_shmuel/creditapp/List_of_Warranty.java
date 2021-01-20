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

public class List_of_Warranty {


    private ArrayList<Warranty> listOfWarranty;
    private Map<String, Object> data;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String email, doc;

    public List_of_Warranty() {
        this.listOfWarranty = new ArrayList<Warranty>();
        this.data = new HashMap<>();
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
        doc = "list of warranty";
    }

    public ArrayList<Warranty> getListOfWarranty() {
        return listOfWarranty;
    }

    public void setListOfWarranty(ArrayList<Warranty> listOfWarranty) {
        this.listOfWarranty = listOfWarranty;
    }

    String addWarranty(String picture1, String picture2, String barCode, String expirationDate, String shopName, String item){
        String key = shopName+barCode;
        Log.d("Debug", "OK" );
        //if(iSExist(key) == false){
        Warranty warranty = new Warranty(shopName, barCode, expirationDate, item, key+"itemReceipt", key+"shopReceipt");
        this.listOfWarranty.add(warranty);
        data.put("warranty", warranty);
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
