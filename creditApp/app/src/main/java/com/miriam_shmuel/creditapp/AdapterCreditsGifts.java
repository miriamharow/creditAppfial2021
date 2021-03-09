package com.miriam_shmuel.creditapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class AdapterCreditsGifts extends ArrayAdapter<Gift_Credit> {
    Context context; // The current context. Used to inflate the layout file.
    ArrayList <Gift_Credit> creditlist; //A List of AndroidFlavor objects to display in a list
    int resource; // layout file

    public AdapterCreditsGifts(Context context, int resource, ArrayList<Gift_Credit> creditlist) {
        super(context, R.layout.item_element, R.id.itemId, creditlist);
        this.context = context;
        this.resource = resource;
        this.creditlist = creditlist;
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
       final LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(resource, null, false);

        // bind to view elements
        TextView cNameStore = (TextView)view.findViewById(R.id.cNameID);
        TextView cDate = (TextView)view.findViewById(R.id.CphoneID);
        TextView cbarCode = (TextView)view.findViewById(R.id.CbarbarcodeID);

        // add contact to list in specific position
        final Gift_Credit creditI = creditlist.get(position);
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

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on the item click on our list view.
                // we are displaying a toast message.
                Intent intentCI = new Intent(getContext(), ShowItemActivity.class);
                intentCI.putExtra("type",creditI.getType());
                intentCI.putExtra("obj", (Serializable) creditI);
                context.startActivity(intentCI);
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View arg0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure to want to delete the receipt?");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                List_of_Credits list_of_credit = new List_of_Credits();
                                list_of_credit.dellete(creditI.getKey());
                                Toast.makeText(context.getApplicationContext(), "delete" , LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });

                builder.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();

                return true;    // <- set to true
            }
        });

        return view;
    }
}



