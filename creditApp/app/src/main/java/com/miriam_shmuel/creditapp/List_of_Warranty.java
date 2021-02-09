package com.miriam_shmuel.creditapp;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.miriam_shmuel.creditapp.AddActivity.instance;

public class List_of_Warranty {


    private ArrayList<Warranty> listOfWarranty;
    private Map<String, Object> data;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String email, docWarranty = "list of warranty";

    public List_of_Warranty() {
        this.listOfWarranty = new ArrayList<Warranty>();
        this.data = new HashMap<>();
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
        docWarranty = "list of warranty";
    }

    public ArrayList<Warranty> getListOfWarranty() {
        return listOfWarranty;
    }

    public void setListOfWarranty(ArrayList<Warranty> listOfWarranty) {
        this.listOfWarranty = listOfWarranty;
    }

    public void addWarranty(String barCode, String expirationDate, String shopName, String itemName, String key) {
       Warranty warranty= new Warranty(shopName, barCode, expirationDate, itemName, key);
        db.collection("user").document(email).collection(docWarranty).document(key).set(warranty)
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
    }

    private void savePic(String key, String typeOfReceipt, Bitmap bitmap) {
        // Create a storage reference from our app
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        // Create a reference to "mountains.jpg"
        StorageReference imageRef = storageRef.child(email+"/"+"list of warranty/"+key+"/"+ key + typeOfReceipt + ".jpg");
        // Create a reference to 'images/mountains.jpg'
        StorageReference documentImagesRef = storageRef.child("images/" + key + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });
    }

    public boolean iSExist(final String barCode, final String expirationDate, final String shopName, final String itemName, final Bitmap picBitmap1, final Bitmap picBitmap2) {
        final String key = shopName + barCode;
        DocumentReference docRef = db.collection("user").document(email).collection(docWarranty).document(key);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Toast.makeText((instance), "the warranty is Exist", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        addWarranty(barCode, expirationDate, shopName, itemName, key);
                        savePic(key, "itemReceipt", picBitmap1);
                        savePic(key, "shopReceipt", picBitmap2);
                        Toast.makeText((instance), "save", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return true;
    }

}
