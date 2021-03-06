package com.miriam_shmuel.creditapp;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
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
    private String email, docGifts;
    private EditItemActivity editItemActivity = EditItemActivity.instance;
    private AddActivity addActivity = AddActivity.instance;

    public List_of_Gifts() {
        this.listOfGifts = new ArrayList<Gift_Credit>();
        this.data = new HashMap<>();
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
        docGifts = "list of gift";
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

    public void addGift(Gift_Credit gift) {

        db.collection("user").document(email).collection(docGifts).document(gift.getKey()).set(gift)
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

    private void savePic(String key, Bitmap bitmap) {
        // Create a storage reference from our app
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        // Create a reference to "mountains.jpg"
        StorageReference imageRef = storageRef.child(email+"/"+docGifts+"/"+key + ".jpg");
        // Create a reference to 'images/mountains.jpg'

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

    public ArrayList<Gift_Credit> readData() {
        CollectionReference docRef = db.collection("user").document(email).collection(docGifts);
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
        return listOfGifts;
    }

    public Gift_Credit iSExist (final String barCode, final String expirationDate, final ArrayList<Shop> shopList, final String value, final String giftName , final Bitmap bitmap, final String state, final String oldKey) {
        final String picture = ""+Timestamp.now().getSeconds();
        final String key = giftName+barCode;
        final Gift_Credit gift = new Gift_Credit(key, barCode, expirationDate, shopList, "gift", value, giftName, picture);
        DocumentReference docRef = db.collection("user").document(email).collection(docGifts).document(key);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists())
                    {
                        if(state.equals("add"))
                        {
                            Toast.makeText(AddActivity.instance, "the gift is Exist", Toast.LENGTH_SHORT).show();
                        }
                        if(state.equals("update"))
                        {
                            Toast.makeText(EditItemActivity.instance, "the gift is Exist", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {
                        addGift(gift);
                        if(state.equals("add"))
                        {
                            savePic(picture, bitmap);
                            Toast.makeText(addActivity, "save", Toast.LENGTH_SHORT).show();
                            addActivity.instance.sendNoti(gift.getKey(), gift.getType());
                        }
                        if(state.equals("update"))
                        {
                            delete(oldKey);
                            Toast.makeText(EditItemActivity.instance, "save", Toast.LENGTH_SHORT).show();
                            editItemActivity.instance.sendNoti(gift.getKey(), gift.getType());
                        }
                    }
                }
                else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return gift;
    }

    public void delete(String key)
    {
        db.collection("user").document(email).collection(docGifts).document(key)
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
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
        StorageReference desertRef = storageRef.child(email+"/"+docGifts+"/"+picture+".jpg");

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
}
