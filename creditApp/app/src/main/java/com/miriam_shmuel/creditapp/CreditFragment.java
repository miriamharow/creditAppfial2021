package com.miriam_shmuel.creditapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
    private AdapterCreditsGifts cAdpter;
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
                            cAdpter = new AdapterCreditsGifts(getActivity(), R.layout.item_element, arrayList);
                            listView.setAdapter(cAdpter);
                            thread();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
        Toast.makeText(getActivity(), ""+arrayList.isEmpty(), Toast.LENGTH_SHORT).show();
        return view;
    }

    public void thread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (i == 1) {
                    if (!threadOff) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SystemClock.sleep(10);    // sleep for 1000ms = 1sec
                            }
                        });
                        SystemClock.sleep(1000);    // sleep for 1000ms = 1sec
                    }
                }
            }

            private void runOnUiThread(Runnable runnable) {
                getCredit();
            }
        }).start();
    }

    private void getCredit() {
        Log.d("test","in getCredit");
        final View view = getLayoutInflater().inflate(R.layout.fragment_credit,  null);
        listView = view.findViewById(R.id.listViewID);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Log.d("test","click");
                Intent intentCI = new Intent(getActivity(), EditItemActivity.class);
                threadOff = true;
                startActivity(intentCI);
            }
        });
        Log.d("test","finish getCredit");
    }
}