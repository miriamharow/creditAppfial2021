package com.miriam_shmuel.creditapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class WarrantyFragment extends Fragment {
    public View view;
    private EditText SearchBar;
    private AdapterWarranties Adapter;
    private ListView listView;
    private ArrayList<Warranty> arrayList;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String email;
    private String characterText;

    public WarrantyFragment() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_credit, container, false);
        arrayList = new  ArrayList<>();
        listView = (ListView)view.findViewById(R.id.listViewID);
        SearchBar = view.findViewById(R.id.edtSearchID);

        Adapter = new AdapterWarranties(getActivity(), R.layout.item_element, arrayList);
        loadWarranty();

        SearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = SearchBar.getText().toString().toLowerCase(Locale.getDefault());
                characterText = text.toLowerCase(Locale.getDefault());
                if (characterText.length() == 0) {
                    arrayList.clear();
                    loadWarranty();
                }
                else {
                    arrayList.clear();
                    searchWarranty();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        //listView.setAdapter(Adapter);
        return view;
    }

    private void loadWarranty() {
        Log.d(TAG, "TAG READ");
        CollectionReference ColRef = db.collection("user").document(email).collection("list of warranty");
        //asynchronously retrieve all documents
        ColRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                               // Adapter = new AdapterWarranties(getActivity(), R.layout.item_element, arrayList);
                                arrayList.add(document.toObject(Warranty.class));
                            }
                            listView.setAdapter(Adapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void searchWarranty() {
        Log.d(TAG, "TAG SERCH");
        //arrayList.clear();
        CollectionReference ColRef = db.collection("user").document(email).collection("list of warranty");
        //asynchronously retrieve all documents
        ColRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.toObject(Warranty.class).getItemName().toLowerCase(Locale.getDefault()).contains(characterText)) {
                                 //   Adapter = new AdapterWarranties(getActivity(), R.layout.item_element, arrayList);
                                    arrayList.add(document.toObject(Warranty.class));
                                }
                            }
                            listView.setAdapter(Adapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public AdapterWarranties getAdapter() {
        return Adapter;
    }

    public void setAdapter(AdapterWarranties adapter) {
        Adapter = adapter;
    }

}