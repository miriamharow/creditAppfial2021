package com.miriam_shmuel.creditapp;

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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.Serializable;
import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

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
                Intent intentCI = new Intent(getContext(), ShowItemActivity.class);
                intentCI.putExtra("type","warranty");
                intentCI.putExtra("obj", (Serializable) warrantyI);
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
                                List_of_Warranty list_of_warranty = new List_of_Warranty();
                                list_of_warranty.delete(warrantyI.getKey());
                                list_of_warranty.deletePicture(warrantyI.getFolder(), warrantyI.getPictureItem());
                                list_of_warranty.deletePicture(warrantyI.getFolder(), warrantyI.getPictureShop());
                                Toast.makeText(context.getApplicationContext(), "delete", LENGTH_SHORT).show();
                                dialog.cancel();
                                Intent intent = new Intent("message_subject_intent");
                                intent.putExtra("type" , String.valueOf("warranty"));
                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
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
                return true;
            }
        });

        return view;
    }
}



