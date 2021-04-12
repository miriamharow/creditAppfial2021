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

public class CreditFragment extends Fragment  {
    public View view;
    private EditText SearchBar;
    private AdapterCreditsGifts Adapter;
    public ListView listView;
    public ArrayList<Gift_Credit> arrayList, fullList;
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
        fullList = new  ArrayList<>();
        listView = (ListView)view.findViewById(R.id.listViewID);
        SearchBar = view.findViewById(R.id.edtSearchID);

        Adapter = new AdapterCreditsGifts(getActivity(), R.layout.item_element, arrayList);
        loadCredit();


        SearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = SearchBar.getText().toString().toLowerCase(Locale.getDefault());
                characterText = text.toLowerCase(Locale.getDefault());
                if (characterText.length() == 0) {
                    arrayList.clear();
                    arrayList.addAll(fullList);
                }
                else {
                    searchCredit();
                }
                listView.setAdapter(Adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }

    private void loadCredit() {
        fullList.clear();
        CollectionReference ColRef = db.collection("user").document(email).collection("list of credit");
        //asynchronously retrieve all documents
        ColRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                arrayList.add(document.toObject(Gift_Credit.class));
                                fullList.add(document.toObject(Gift_Credit.class));
                            }
                            listView.setAdapter(Adapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void searchCredit() {
        ArrayList<Gift_Credit> temp = new  ArrayList<>();
        for (int i = 0; i < fullList.size(); i++) {
            if ((fullList.get(i).getShopName().get(0).toString().toLowerCase(Locale.getDefault()).contains(characterText))
            || (fullList.get(i).getValue().toLowerCase(Locale.getDefault()).equals(characterText))) {
                temp.add(fullList.get(i));
            }
        }

        arrayList.clear();
        arrayList.addAll(temp);
    }

    public AdapterCreditsGifts getAdapter() {
        return Adapter;
    }

    public void setAdapter(AdapterCreditsGifts adapter) {
        Adapter = adapter;
    }


}
