package com.miriam_shmuel.creditapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class CreditFragment extends Fragment  {
    public static List_of_Credits instance;
    private List_of_Credits listCredit;
    private Map<String, Object> data;
    public View view;



    private AdapterCreditsGifts Adapter;
    public ListView listView;
    public ArrayList<Gift_Credit> arrayList;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String email;

    private boolean threadOff = false;
    private int i =1;


    public CreditFragment() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_credit, container, false);
        arrayList = new  ArrayList<>();
        listView = (ListView)view.findViewById(R.id.listViewID);

        CollectionReference ColRef = db.collection("user").document(email).collection("list of credit");
        //asynchronously retrieve all documents
        ColRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i=0;
                            Log.d("test","in 1");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                arrayList.add(document.toObject(Gift_Credit.class));
                                Log.d("test","number array "+i++);
                            }
                            Adapter = new AdapterCreditsGifts(getActivity(), R.layout.item_element, arrayList);
                            listView.setAdapter(Adapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
        return view;
    }
    public AdapterCreditsGifts getAdapter() {
        return Adapter;
    }

    public void setAdapter(AdapterCreditsGifts adapter) {
        Adapter = adapter;
    }
}