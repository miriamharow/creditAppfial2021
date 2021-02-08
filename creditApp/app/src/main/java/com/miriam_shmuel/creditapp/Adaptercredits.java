package com.miriam_shmuel.creditapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Adaptercredits extends ArrayAdapter<Gift_Credit> {
    /*
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     **/
    Context context; // The current context. Used to inflate the layout file.
    ArrayList <Gift_Credit> creditlist; //A List of AndroidFlavor objects to display in a list
    int resource; // layout file

    public Adaptercredits(Context context, int resource, ArrayList<Gift_Credit> contactlist) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // this is a custom adapter for two TextViews and an ImageView not
        super(context, resource, contactlist);
        this.context = context;
        this.resource = resource;
        this.creditlist = contactlist;
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position The position in the list of data that should be displayed in the
     *                 list item view. like index
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */

    @Nullable
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent)
    {
        // get view
       LayoutInflater layoutInflater = LayoutInflater.from(context);
       View view = layoutInflater.inflate(resource, null, false);

        // bind to view elements
        TextView cNameStore = (TextView)view.findViewById(R.id.cNameID);
        TextView cDate = (TextView)view.findViewById(R.id.CphoneID);
        TextView cbarCode = (TextView)view.findViewById(R.id.CbarbarcodeID);

        // add contact to list in specific position
        Gift_Credit creditI = creditlist.get(position);
        cNameStore.setText(creditI.getShopName().get(0).getName());
        cDate.setText(creditI.getExpirationDate());
        cbarCode.setText(creditI.getValue());

        return view;
    }
}



