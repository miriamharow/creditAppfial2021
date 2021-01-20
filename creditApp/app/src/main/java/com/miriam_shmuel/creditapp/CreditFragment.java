package com.miriam_shmuel.creditapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;

public class CreditFragment extends Fragment  {
    public static List_of_Credits instance;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //-----------------ADAPTER LIST VIEW-----------------
        // initialize Adapter
        //arrayList = listCredit.getListOfCredit();

        List_of_Credits listOfcredit = new List_of_Credits();
        listOfcredit.readData();


        View view = inflater.inflate(R.layout.fragment_credit, container, false);

        Shop s = new Shop("Zara");
        ArrayList<Shop> shopName = new ArrayList<Shop> ();
        shopName.add(s);

        arrayList = new  ArrayList<>();
        arrayList.add( new Gift_Credit("1234", "1234", "1/2/3", shopName, "c", "12", ""));
        //Log.d("Debug", "array " +arrayList.get(0).getBarCode());
        cAdpter = new Adaptercredits(getActivity(), R.layout.item_element, arrayList);
        //view = getLayoutInflater().inflate(R.layout.fragment_credit, null);
        listView = (ListView)view.findViewById(R.id.listViewID);
        listView.setAdapter(cAdpter);

        //---------------------------------------------------


        return view;
    }

}