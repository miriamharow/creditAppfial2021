package com.miriam_shmuel.creditapp;

import android.os.Bundle;
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

import static android.content.ContentValues.TAG;

public class GiftFragment extends Fragment {
    //public View view;
    private AdapterCreditsGifts cAdpter;
    private ListView listView;
    private ArrayList<Gift_Credit> arrayList;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String email;

    public GiftFragment() {
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
        final View view = inflater.inflate(R.layout.fragment_gift, container, false);
        listView = (ListView)view.findViewById(R.id.listViewID);
        arrayList = new ArrayList<>();
        CollectionReference ColRef = db.collection("user").document(email).collection("list of gift");
        //asynchronously retrieve all documents
        ColRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                arrayList.add(document.toObject(Gift_Credit.class));
                            }
                            cAdpter = new AdapterCreditsGifts(getActivity(), R.layout.item_element, arrayList);
                            listView.setAdapter(cAdpter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                /*Gift_Credit gift = arrayList.get(position);
                Intent intentCI = new Intent(getActivity(), EditItemActivity.class);
                intentCI.putExtra("gift", (Parcelable) gift);
                startActivity(intentCI);*/
                Toast.makeText(getActivity(), "First Fragment", Toast.LENGTH_LONG).show();
            }
        });

        return view;


    }

}