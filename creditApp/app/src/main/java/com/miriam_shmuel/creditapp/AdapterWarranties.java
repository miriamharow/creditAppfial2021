package com.miriam_shmuel.creditapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

public class AdapterWarranties extends ArrayAdapter<Warranty> {
    /*
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     **/
    Context context; // The current context. Used to inflate the layout file.
    ArrayList <Warranty> warrantylist; //A List of AndroidFlavor objects to display in a list
    int resource; // layout file

    public AdapterWarranties(Context context, int resource, ArrayList<Warranty> contactlist) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // this is a custom adapter for two TextViews and an ImageView not
        super(context, R.layout.item_element, R.id.itemId, contactlist);
        this.context = context;
        this.resource = resource;
        this.warrantylist = contactlist;
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
        final Warranty warrantyI = warrantylist.get(position);
            cNameStore.setText(warrantyI.getItemName());
            cDate.setText(warrantyI.getShopName());
            cbarCode.setText(warrantyI.getExpirationDate());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on the item click on our list view.
                // we are displaying a toast message.
                Intent intentCI = new Intent(getContext(), EditItemActivity.class);
                intentCI.putExtra("type","warranty");
                intentCI.putExtra("obj", (Serializable) warrantyI);
                context.startActivity(intentCI);
            }
        });



        return view;
    }
}



