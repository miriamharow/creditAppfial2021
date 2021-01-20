package com.miriam_shmuel.creditapp;

import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static android.content.ContentValues.TAG;

public class List_of_Credits {
    private ArrayList<Gift_Credit> listOfCredit;
    private Map<String, Object> data;
    private FirebaseFirestore db;
    private FirebaseUser user;
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

    public String addCredit(String picture, String barCode, String expirationDate, ArrayList<Shop> shopName, String value) {
        String key = shopName.get(0).getName() + barCode;
        Gift_Credit credit = new Gift_Credit(key, barCode, expirationDate, shopName, "credit", value, null);
        this.listOfCredit.add(credit);
        data.put("credit", credit);
        db.collection("user").document(email).collection(doc).document(key).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

        //}
        return key;
    }


    public ArrayList<Gift_Credit> readData() {
        CollectionReference docRef = db.collection("user").document(email).collection(doc);
        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Map<String, Object> d = document.getData();
                                String str =d.toString();

                            }
                        }
                    }
                });
        return listOfCredit;
    }


    public void iSExist(final String picture, final String barCode, final String expirationDate, final ArrayList<Shop> shopName, final String value) {
        String key = shopName.get(0).getName() + barCode;
        DocumentReference docRef = db.collection("user").document(email).collection(doc).document(key);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                    } else {
                        addCredit( picture, barCode,  expirationDate, shopName,  value);
                    }
                }
                else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

}
