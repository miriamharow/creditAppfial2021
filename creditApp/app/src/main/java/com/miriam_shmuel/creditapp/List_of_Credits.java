package com.miriam_shmuel.creditapp;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class List_of_Credits {
    private ArrayList<Gift_Credit> listOfCredit;
    private  Map<String, Object> data;
    private String type;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore db;

    public List_of_Credits() {
        this.listOfCredit = new ArrayList<Gift_Credit>();
        this.type = "Credits";
        this.data = new HashMap<>();
    }

    public ArrayList<Gift_Credit> getListOfCredit() {
        return listOfCredit;
    }

    public void setListOfCredit(ArrayList<Gift_Credit> listOfCredit) {
        this.listOfCredit = listOfCredit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String addCredit(String picture, String type, String barCode, String expirationDate, ArrayList<Shop> shopName) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        db = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        String key = shopName.get(0).getName()+barCode;

        Gift_Credit credit = new Gift_Credit(key, type, barCode, expirationDate, shopName);
        this.listOfCredit.add(credit);

        // Add a new document with a generated id.
        data.put("credit", credit);
        db.collection(email).document("list of credit").collection(key)
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
        return key;
    }

    public void readData(){
        DocumentReference docRef = db.collection("list of credits").document("s@gmail.com");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                List_of_Credits lc = documentSnapshot.toObject(List_of_Credits.class);
                listOfCredit = lc.listOfCredit;
                if (listOfCredit.isEmpty()){
                    Log.d("Debug", "OK" );
                }
                else{
                    Log.d("Debug", "Not Empty" );
                }
            }
        });
    }

}
