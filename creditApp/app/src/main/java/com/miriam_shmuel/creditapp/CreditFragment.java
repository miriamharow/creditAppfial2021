package com.miriam_shmuel.creditapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class CreditFragment extends Fragment {
    public View view;
    private List_of_Credits listCredit;
    private Adaptercredits cAdpter;
    private ListView listView;
    private ArrayList<Gift_Credit> arrayList;

    public CreditFragment() {
    // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //-----------------ADAPTER LIST VIEW-----------------
        // initialize Adapter
        //arrayList = listCredit.getListOfCredit();
        Shop s = new Shop("Zara");
        ArrayList<Shop> shopName = new ArrayList<Shop> ();
        shopName.add(s);

       // Gift_Credit c = new Gift_Credit("1234", "1234", "1/2/3", shopName, "c");
        arrayList = new  ArrayList<>();

        cAdpter = new Adaptercredits(getContext(), R.layout.item_element, arrayList);
        view = getLayoutInflater().inflate(R.layout.fragment_credit, null);
        listView = view.findViewById(R.id.listViewID);
        listView.setAdapter(cAdpter);

        //arrayList.add(c);
        cAdpter.notifyDataSetChanged();
        //---------------------------------------------------
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_credit, container, false);
    }

}