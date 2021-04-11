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
import java.util.Locale;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.widget.Toast.LENGTH_SHORT;

public class CreditFragment extends Fragment  {
    public View view;
    private EditText SearchBar;
    private AdapterCreditsGifts Adapter;
    public ListView listView;
    public ArrayList<Gift_Credit> arrayList;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String email;
    private String characterText;

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
        SearchBar = view.findViewById(R.id.edtSearchID);

        Adapter = new AdapterCreditsGifts(getActivity(), R.layout.item_element, arrayList);
        loadCretdit();

        SearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = SearchBar.getText().toString().toLowerCase(Locale.getDefault());
                characterText = text.toLowerCase(Locale.getDefault());
                if (characterText.length() == 0) {
                    arrayList.clear();
                    loadCretdit();
                }
                else {
                    arrayList.clear();
                    searchCredit();
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
       // listView.setAdapter(Adapter);

        return view;
    }

    private void loadCretdit() {
        Log.d(TAG, "TAG READ");
        CollectionReference ColRef = db.collection("user").document(email).collection("list of credit");
        //asynchronously retrieve all documents
        ColRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                arrayList.add(document.toObject(Gift_Credit.class));
                            }
                            listView.setAdapter(Adapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    private void searchCredit() {
        Log.d(TAG, "TAG SERCH");
        CollectionReference ColRef = db.collection("user").document(email).collection("list of credit");
        //asynchronously retrieve all documents
        ColRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.toObject(Gift_Credit.class).getShopName().get(0).toString().toLowerCase(Locale.getDefault()).contains(characterText)) {
                                    arrayList.add(document.toObject(Gift_Credit.class));
                                }
                            }
                            listView.setAdapter(Adapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    public AdapterCreditsGifts getAdapter() {
        return Adapter;
    }

    public void setAdapter(AdapterCreditsGifts adapter) {
        Adapter = adapter;
    }


}
