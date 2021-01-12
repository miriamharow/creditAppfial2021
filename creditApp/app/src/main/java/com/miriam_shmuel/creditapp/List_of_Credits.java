package com.miriam_shmuel.creditapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class List_of_Credits {
    private ArrayList<Gift_Credit> listOfCredit;
    private  Map<String, Object> data;
    private FirebaseFirestore  db;
    private FirebaseUser  user;
    private String email, doc;

    public List_of_Credits() {
        this.listOfCredit = new ArrayList<Gift_Credit>();
        this.data = new HashMap<>();
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
        doc = "list of credit";
    }

    public ArrayList<Gift_Credit> getListOfCredit() {
        return listOfCredit;
    }

    public void setListOfCredit(ArrayList<Gift_Credit> listOfCredit) {
        this.listOfCredit = listOfCredit;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public String addCredit(String picture, String barCode, String expirationDate, ArrayList<Shop> shopName) {
        String key = shopName.get(0).getName()+barCode;
        Log.d("Debug", "OK" );
        //if(iSExist(key) == false){
            Gift_Credit credit = new Gift_Credit(key, barCode, expirationDate, shopName, "credit");
            this.listOfCredit.add(credit);

            data.put("credit", credit);

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

    public void readData(){
        DocumentReference docRef = db.collection(email).document(doc);
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

    public boolean iSExist(final String key) {
        boolean isexist = false;
        db.collection(email).document(doc).collection(key).get();
        CollectionReference citiesRef = db.collection(email).document(doc).collection(key);
        citiesRef.whereIn("key", Arrays.asList(key));
        if(citiesRef == null)
            return false;

        return true;
    }
}
