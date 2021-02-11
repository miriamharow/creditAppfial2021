package com.miriam_shmuel.creditapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AdapterCreditsGifts extends ArrayAdapter<Gift_Credit> {
    Context context; // The current context. Used to inflate the layout file.
    ArrayList <Gift_Credit> creditlist; //A List of AndroidFlavor objects to display in a list
    int resource; // layout file

    public AdapterCreditsGifts(Context context, int resource, ArrayList<Gift_Credit> contactlist) {
        super(context, R.layout.item_element, R.id.itemId, contactlist);
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
        if(creditI.getType().equals("credit")) {
            cNameStore.setText(creditI.getShopName().get(0).getName());
            cDate.setText(creditI.getExpirationDate());
            cbarCode.setText(creditI.getValue());
        }
        else if (creditI.getType().equals("gift")){
            cNameStore.setText(creditI.getGiftName());
            cDate.setText(creditI.getExpirationDate());
            cbarCode.setText(creditI.getValue());

        }
        View item = layoutInflater.inflate(R.layout.item_element, parent, false);
        ImageButton btnRow = item.findViewById(R.id.btnEditID);
        btnRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "First Fragment", Toast.LENGTH_LONG).show();
            }
        });


        return view;
    }

    /*View row = layoutInflater.inflate(R.layout.item_element,parent,false);
        row.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
                /*Gift_Credit gift = arrayList.get(position);
                Intent intentCI = new Intent(getActivity(), EditItemActivity.class);
                intentCI.putExtra("gift", (Parcelable) gift);
                startActivity(intentCI);
            Toast.makeText(context, "First Fragment", Toast.LENGTH_LONG).show();
        }
    });*/

}



