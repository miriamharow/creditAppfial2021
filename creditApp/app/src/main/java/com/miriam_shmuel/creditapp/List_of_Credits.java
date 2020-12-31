package com.miriam_shmuel.creditapp;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class List_of_Credits {
    private ArrayList<Gift_Credit> listOfCredit;
    private String type;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore db;

    public List_of_Credits() {
        this.listOfCredit = new ArrayList<Gift_Credit>();
        this.type = "Credits";
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

    public int addCredit(String picture, String type, String barCode, String expirationDate, ArrayList<Shop> shopName) {
        int success = 0;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        db=FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        Gift_Credit credit = new Gift_Credit(picture, type, barCode, expirationDate, shopName);
        db.collection("list of credits").document(email).set(credit);



        /*.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddCreditOrGiftActivity, "add ", Toast.LENGTH_LONG).show();
                //Log.d("", "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, "error", Toast.LENGTH_LONG).show();
                        Log.w("", "Error adding document", e);

                });*/
        return 0;

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
