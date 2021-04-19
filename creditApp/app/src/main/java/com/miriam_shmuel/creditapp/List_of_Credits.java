package com.miriam_shmuel.creditapp;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
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

public class List_of_Credits {
    private ArrayList<Gift_Credit> listOfCredit;
    private Map<String, Object> data;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String email, docCredits;
    private EditItemActivity editItemActivity = EditItemActivity.instance;
    private AddActivity addActivity = AddActivity.instance;
    public static final int ONETIME_ALARM_CODE = 0;

    public List_of_Credits() {
        this.listOfCredit = new ArrayList<Gift_Credit>();
        this.data = new HashMap<>();
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
        docCredits = "list of credit";
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

    public Gift_Credit addCredit( Gift_Credit credit) {
        db.collection("user").document(email).collection(docCredits).document(credit.getKey()).set(credit)
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
        return credit;
    }


    private void savePic(String key, Bitmap bitmap) {
        // Create a storage reference from our app
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        // Create a reference to "mountains.jpg"
        StorageReference imageRef = storageRef.child(email+"/"+docCredits+"/"+key + ".jpg");
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

    public Gift_Credit iSExist(final String barCode, final String expirationDate, final ArrayList<Shop> shopName, final String value, final Bitmap bitmap, final String state, final String oldKey) {
        final String picture = ""+Timestamp.now().getSeconds();
        final String key =shopName.get(0).getName()+barCode;
        final Gift_Credit credit = new Gift_Credit(key, barCode, expirationDate, shopName, "credit", value, null,picture);
        DocumentReference docRef = db.collection("user").document(email).collection(docCredits).document(key);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if(state.equals("add"))
                        {
                            Toast.makeText(addActivity, "the credit exists", Toast.LENGTH_SHORT).show();
                        }
                        if(state.equals("update"))
                        {
                            Toast.makeText(EditItemActivity.instance, "the credit exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                       if(state.equals("add"))
                       {
                           addCredit(credit);
                           savePic( picture, bitmap);
                           Toast.makeText((addActivity), "save", Toast.LENGTH_SHORT).show();
                           addActivity.sendNoti(credit.getKey(), credit.getType());
                       }
                       if(state.equals("update"))
                       {
                           delete(oldKey);
                           addCredit(credit);
                           Toast.makeText(EditItemActivity.instance, "save", Toast.LENGTH_SHORT).show();
                           editItemActivity.sendNoti(credit.getKey(), credit.getType());
                       }
                    }
                }
                else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }


        });
        return credit;
    }

    public void delete(String key)
    {
        db.collection("user").document(email).collection(docCredits).document(key)
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
          Log.d("debug", "dellete");
            }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

    }

    public void deletePicture(String picture){
        // Create a storage reference from our app
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

// Create a reference to the file to delete
        StorageReference desertRef = storageRef.child(email+"/"+docCredits+"/"+picture+".jpg");

// Delete the file
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });
    }

    /*public void deleteNotification (int notification_id)
    {
        NotificationManager notificationManager = getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notification_id);
    }*/
}



